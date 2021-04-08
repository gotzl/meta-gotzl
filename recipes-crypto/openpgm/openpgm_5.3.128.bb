SUMMARY = ""
LICENSE = "LGPL"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"

SRC_URI = "git://github.com/steve-o/openpgm.git;protocol=git;branch=master"
SRCREV = "cd3456a5c437bb542d9af2a37778ec7941dc05a5"

S = "${WORKDIR}/git/${PN}/pgm"

inherit autotools

do_configure_prepend() {
    cp ${S}/openpgm-5.2.pc.in ${S}/openpgm-5.3.pc.in
    sed -i 's,^AC_CHECK_FILES,# AC_CHECK_FILES,g' ${S}/configure.ac
}
