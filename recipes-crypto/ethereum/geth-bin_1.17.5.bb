SUMMARY = "Official Go implementation of the Ethereum protocol"
LICENSE = "LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

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

HASH = "9621c6ad"
def geth_md5(arch):
    HASHES = {
        "aarch64": "d270e6ee5a4d0ddbdf9cbbfc3829bc0b",
        "i686": "5bbeeedb310ae499a613317efac401a0",
        "x86_64": "925de60686b5eaa15d5a21acfc878ecf"
    }
    return get_by_arch(HASHES, arch)

def geth_url(arch):
    URLS = {
        "aarch64": "https://gethstore.blob.core.windows.net/builds/geth-linux-arm64-${PV}-${HASH}.tar.gz",
        "i686": "https://gethstore.blob.core.windows.net/builds/geth-linux-386-${PV}-${HASH}.tar.gz",
        "x86_64": "https://gethstore.blob.core.windows.net/builds/geth-linux-amd64-${PV}-${HASH}.tar.gz",
    }
    return get_by_arch(URLS, arch)

python () {
    pv = d.getVar("PV", True)
    target = d.getVar("TARGET_ARCH", True)
    geth_uri = ("%s;md5sum=%s;downloadname=%s" %
                 (geth_url(target), geth_md5(target),
                  d.getVar("PN", True) + "-" + pv + "-" + target + ".tar.gz"))
    src_uri = d.getVar("SRC_URI", True).split()
    d.setVar("SRC_URI", ' '.join(src_uri + [geth_uri]))
    d.setVar("S", "%s/geth-linux-%s-%s-%s" % (d.getVar("UNPACKDIR"), {"aarch64": "arm64"}[target], pv, d.getVar("HASH")))
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/geth ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/geth.service ${D}${systemd_unitdir}/system    
}

SYSTEMD_SERVICE:${PN} = "geth.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

INSANE_SKIP:${PN} = "already-stripped"
