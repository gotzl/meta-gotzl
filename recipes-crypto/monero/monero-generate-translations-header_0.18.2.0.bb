SUMMARY = ""
LICENSE = "monero-project"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=25778095b6d857a291b6d8f9769d8a20"

SRC_URI = "git://github.com/monero-project/monero.git;protocol=https;branch=release-v0.18"
SRCREV = "99be9a044f3854f339548e2d99c539c18d7b1b01"

S = "${WORKDIR}/git/translations"

BBCLASSEXTEND = "native"

inherit cmake

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${B}/generate_translations_header ${D}${bindir}
}
