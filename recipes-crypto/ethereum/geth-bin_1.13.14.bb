SUMMARY = "Official Go implementation of the Ethereum protocol"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

FILESEXTRAPATHS:prepend := "${THISDIR}/geth:"
SRC_URI:append = " \
    file://geth.service \
"

inherit systemd

def get_by_arch(hashes, arch):
    try:
        return hashes[arch]
    except:
        raise bb.parse.SkipRecipe("Unsupported arch: %s" % arch)

HASH = "2bd6bd01"
def geth_md5(arch):
    HASHES = {
        "aarch64": "cf5cdae9ba80cb3137f36242a278a9f7",
        "armv7": "6c98c08d213555f406f2aad490dbc9be",
        "i686": "d067227ee306de1f855d0663d5fe2647",
        "x86_64": "28681cf9d79e8120a88be51d2ec318fe"
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