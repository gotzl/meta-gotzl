SUMMARY = "Official Go implementation of the Ethereum protocol"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI:append = " \
    file://geth.service \
"

inherit systemd

def get_by_arch(hashes, arch):
    try:
        return hashes[arch]
    except:
        raise bb.parse.SkipRecipe("Unsupported arch: %s" % arch)

HASH = "25c9b49f"
def geth_md5(arch):
    HASHES = {
        "aarch64": "7a0bacb14b88c250fcbf1988041a3cc8",
        "armv7": "da3c8cee3b11ff2c78a729aef1e46b1d",
        "i686": "f6bf559b855f6e1584d74e12f5768e36",
        "x86_64": "d77405bf3bc024a624bcf9f79812e61d",
    }
    return get_by_arch(HASHES, arch)

def geth_url(arch):
    URLS = {
        "aarch64": "https://gethstore.blob.core.windows.net/builds/geth-linux-arm64-${PV}-${HASH}.tar.gz",
        "armv7": "https://gethstore.blob.core.windows.net/builds/geth-linux-arm7-${PV}-${HASH}.tar.gz",
        "i686": "https://gethstore.blob.core.windows.net/builds/geth-linux-386-${PV}-${HASH}.tar.gz",
        "x86_64": "https://gethstore.blob.core.windows.net/builds/geth-linux-amd64-${PV}-${HASH}.tar.gz",
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

SYSTEMD_SERVICE:${PN} = "geth.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
