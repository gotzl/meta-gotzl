SUMMARY = ""
LICENSE = "monero-project"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=4c2dca935bfb2f56cf2164bee83b1934"

SRC_URI = "git://github.com/monero-project/monero.git;protocol=git;branch=release-v0.17"
SRCREV = "8fef32e45c80aec41f25be9d1d8fb75adc883c64"

S = "${WORKDIR}/git/translations"

BBCLASSEXTEND = "native"

inherit cmake

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${B}/generate_translations_header ${D}${bindir}
}
