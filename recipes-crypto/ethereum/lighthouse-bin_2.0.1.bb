SUMMARY = "Rust Ethereum 2.0 Client"
LICENSE = "Apache"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=139bc4b5f578ecacea78dc7b7ad3ed3c"

SRC_URI_append = " \
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
        "aarch64": "1fc9c47c859896716d3ad023552d4c8d",
        "x86_64": "bf16471a0769c89d62011f17fd6c530d",
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
    pv_uri = pv[0:4] + '-' + pv[4:6] + '-' + pv[6:8]
    target = d.getVar("TARGET_ARCH", True)
    lighthouse_uri = ("%s;md5sum=%s;downloadname=%s" %
                 (lighthouse_url(target), lighthouse_md5(target),
                  d.getVar("PN", True) + "-" + pv + "-" + target + ".tar.gz"))
    src_uri = d.getVar("SRC_URI", True).split()
    lighthouse_extract_path = lighthouse_url(target).split('/')[-1].replace('.tar.gz', '')
    d.setVar("SRC_URI", ' '.join(src_uri + [lighthouse_uri]))
    d.setVar("S", "${{WORKDIR}}/{}".format(lighthouse_extract_path))
    d.appendVarFlag("S", "vardeps", " lighthouse_url")
    d.appendVarFlag("S", "vardepsexclude", " WORKDIR")
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/lighthouse ${D}${bindir}

    install -d ${D}${sysconfdir}/lighthouse
    install -m 0644 ${WORKDIR}/lighthouse-beacon-node.conf ${D}${sysconfdir}/lighthouse
    install -m 0644 ${WORKDIR}/lighthouse-validator.conf ${D}${sysconfdir}/lighthouse

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/lighthouse-beacon-node.service ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/lighthouse-validator.service ${D}${systemd_unitdir}/system
}

PACKAGES_append = " ${SYSTEMD_PACKAGES}"
RDEPENDS_${PN}_append = " ${SYSTEMD_PACKAGES}"

SYSTEMD_PACKAGES = "${PN}-beacon-node ${PN}-validator"
SYSTEMD_SERVICE_${PN}-beacon-node = "lighthouse-beacon-node.service"
SYSTEMD_SERVICE_${PN}-validator = "lighthouse-validator.service"
SYSTEMD_AUTO_ENABLE_${PN}-beacon-node = "disable"
SYSTEMD_AUTO_ENABLE_${PN}-validator = "disable"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "-g 1101 ether"
USERADD_PARAM_${PN} = "-u 1101 -g ether -M -r -s /bin/false ether"
