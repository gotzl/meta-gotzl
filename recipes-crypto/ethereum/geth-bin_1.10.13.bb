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
        "aarch64": "78558d1ea0f7d1e49e98bef325d2cbc4",
        "armv7": "70267a883566ef3df8e188c61452d5ea",
        "i686": "a6128d8abc87e19d44306822ef050ece",
        "x86_64": "a1dfdd549ee779654c196d1b01e0ca67",
    }
    return get_by_arch(HASHES, arch)

def geth_url(arch):
    URLS = {
        "aarch64": "https://gethstore.blob.core.windows.net/builds/geth-linux-arm64-${PV}-7a0c19f8.tar.gz",
        "armv7": "https://gethstore.blob.core.windows.net/builds/geth-linux-arm7-${PV}-7a0c19f8.tar.gz",
        "i686": "https://gethstore.blob.core.windows.net/builds/geth-linux-386-${PV}-7a0c19f8.tar.gz",
        "x86_64": "https://gethstore.blob.core.windows.net/builds/geth-linux-amd64-${PV}-7a0c19f8.tar.gz",
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
