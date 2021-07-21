SUMMARY = "Official Go implementation of the Ethereum protocol"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI_append = " \
    file://geth.service \
"

inherit systemd

def get_by_arch(hashes, arch):
    try:
        return hashes[arch]
    except:
        raise bb.parse.SkipRecipe("Unsupported arch: %s" % arch)

def geth_md5(arch):
    HASHES = {
        "aarch64": "00d2daf050476f2c3a0f08f30480b8e7",
        "armv7": "17ed9da87f97fdf7cf52ed21f2c85311",
        "i686": "6e2c70728db509163682a7843e22f76f",
        "x86_64": "b02cba8d5a49df9fe84b1f33f4d02971",
    }
    return get_by_arch(HASHES, arch)

def geth_url(arch):
    URLS = {
        "aarch64": "https://gethstore.blob.core.windows.net/builds/geth-linux-arm64-${PV}-aa637fd3.tar.gz",
        "armv7": "https://gethstore.blob.core.windows.net/builds/geth-linux-arm7-${PV}-aa637fd3.tar.gz",
        "i686": "https://gethstore.blob.core.windows.net/builds/geth-linux-386-${PV}-aa637fd3.tar.gz",
        "x86_64": "https://gethstore.blob.core.windows.net/builds/geth-linux-amd64-${PV}-aa637fd3.tar.gz",
    }
    return get_by_arch(URLS, arch)

python () {
    pv = d.getVar("PV", True)
    pv_uri = pv[0:4] + '-' + pv[4:6] + '-' + pv[6:8]
    target = d.getVar("TARGET_ARCH", True)
    geth_uri = ("%s;md5sum=%s;downloadname=%s" %
                 (geth_url(target), geth_md5(target),
                  d.getVar("PN", True) + "-" + pv + "-" + target + ".tar.gz"))
    src_uri = d.getVar("SRC_URI", True).split()
    geth_extract_path = geth_url(target).split('/')[-1].replace('.tar.gz', '')
    d.setVar("SRC_URI", ' '.join(src_uri + [geth_uri]))
    d.setVar("S", "${{WORKDIR}}/{}".format(geth_extract_path))
    d.appendVarFlag("S", "vardeps", " geth_url")
    d.appendVarFlag("S", "vardepsexclude", " WORKDIR")
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/geth ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/geth.service ${D}${systemd_unitdir}/system    
}

SYSTEMD_SERVICE_${PN} = "geth.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
