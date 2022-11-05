SUMMARY = ""
LICENSE = "monero-project"
LIC_FILES_CHKSUM = "file://LICENSE;md5=25778095b6d857a291b6d8f9769d8a20"

SRC_URI = "gitsm://github.com/monero-project/monero.git;protocol=https;branch=release-v0.18"
SRCREV = "66184f30859796f3c7c22f9497e41b15b5a4a7c9"

S = "${WORKDIR}/git"
DEPENDS = "boost openssl zeromq openpgm unbound monero-generate-translations-header-native"

inherit cmake systemd useradd

EXTRA_OECMAKE=" \
    -D BUILD_DOCUMENTATION=OFF \
    -D BUILD_TESTS=OFF \
    -D CMAKE_BUILD_TYPE=release \
    -D MONERO_PARALLEL_COMPILE_JOBS=4 \
    -D MONERO_PARALLEL_LINK_JOBS=2 \
"

do_configure:prepend() {
    # use the already build `generate_translations_header` binary
    sed -i 's,COMMAND \S*,COMMAND ${STAGING_DIR_NATIVE}/usr/bin/generate_translations_header,g' ${S}/translations/CMakeLists.txt
}

do_install:append() {
    install -d ${D}${sysconfdir}
    install -m 644 ${S}/utils/conf/monerod.conf ${D}${sysconfdir}
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/utils/systemd/monerod.service ${D}${systemd_unitdir}/system

    # the monero user has no home dir ...
    sed -i -e 's/^WorkingDirectory=/# WorkingDirectory=/g' \
           ${D}${systemd_unitdir}/system/monerod.service    
}

SYSTEMD_SERVICE:${PN} = "monerod.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "-g 1102 monero"
USERADD_PARAM:${PN} = "-u 1102 -g monero -M -r -s /bin/false monero"
