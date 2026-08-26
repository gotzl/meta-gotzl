SUMMARY = "Rust Ethereum 2.0 Client"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=139bc4b5f578ecacea78dc7b7ad3ed3c"

SRC_URI:append = " \
    file://LICENSE \
    file://lighthouse-beacon-node.conf \
    file://lighthouse-beacon-node.service \
    file://lighthouse-validator.conf \
    file://lighthouse-validator.service \
"

inherit systemd useradd

def get_by_arch(hashes, arch):
    try:
        return hashes[arch]
    except:
        raise bb.parse.SkipRecipe("Unsupported arch: %s" % arch)

def lighthouse_md5(arch):
    HASHES = {
        "aarch64": "8e337413d3a19da7d5cbdb293d731063",
        "x86_64": "ea2834e9415159796ff5da11ab5cae0b",
    }
    return get_by_arch(HASHES, arch)

def lighthouse_url(arch):
    URLS = {
        "aarch64": "https://github.com/sigp/lighthouse/releases/download/v${PV}/lighthouse-v${PV}-aarch64-unknown-linux-gnu.tar.gz",
        "x86_64": "https://github.com/sigp/lighthouse/releases/download/v${PV}/lighthouse-v${PV}-x86_64-unknown-linux-gnu.tar.gz",
    }
    return get_by_arch(URLS, arch)

python () {
    pv = d.getVar("PV", True)
    target = d.getVar("TARGET_ARCH", True)
    lighthouse_uri = ("%s;md5sum=%s;downloadname=%s" %
                 (lighthouse_url(target), lighthouse_md5(target),
                  d.getVar("PN", True) + "-" + pv + "-" + target + ".tar.gz"))
    src_uri = d.getVar("SRC_URI", True).split()
    d.setVar("SRC_URI", ' '.join(src_uri + [lighthouse_uri]))
    d.setVar("S", d.getVar("UNPACKDIR"))
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/lighthouse ${D}${bindir}

    install -d ${D}${sysconfdir}/lighthouse
    install -m 0644 ${UNPACKDIR}/lighthouse-beacon-node.conf ${D}${sysconfdir}/lighthouse
    install -m 0644 ${UNPACKDIR}/lighthouse-validator.conf ${D}${sysconfdir}/lighthouse

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/lighthouse-beacon-node.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/lighthouse-validator.service ${D}${systemd_unitdir}/system
}

PACKAGES:append = " ${SYSTEMD_PACKAGES}"
RDEPENDS:${PN}:append = " ${SYSTEMD_PACKAGES}"

SYSTEMD_PACKAGES = "${PN}-beacon-node ${PN}-validator"
SYSTEMD_SERVICE:${PN}-beacon-node = "lighthouse-beacon-node.service"
SYSTEMD_SERVICE:${PN}-validator = "lighthouse-validator.service"
SYSTEMD_AUTO_ENABLE:${PN}-beacon-node = "disable"
SYSTEMD_AUTO_ENABLE:${PN}-validator = "disable"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "-g 1101 ether"
USERADD_PARAM:${PN} = "-u 1101 -g ether -M -r -s /bin/false ether"

REMOVE_LIBTOOL_LA = "0"
