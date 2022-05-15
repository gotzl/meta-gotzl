SUMMARY = ""
LICENSE = "monero-project"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4c2dca935bfb2f56cf2164bee83b1934"

SRC_URI = "gitsm://github.com/monero-project/monero.git;protocol=https;branch=release-v0.17"
SRCREV = "424e4de16b98506170db7b0d7d87a79ccf541744"

S = "${WORKDIR}/git"
DEPENDS = "boost openssl zeromq openpgm unbound monero-generate-translations-header-native"

inherit cmake systemd useradd

EXTRA_OECMAKE=" \
    -D BUILD_DOCUMENTATION=OFF \
    -D BUILD_TESTS=OFF \
    -D CMAKE_BUILD_TYPE=release \
"

do_configure:prepend() {
    # use the already build `generate_translations_header` binary
    sed -i 's,COMMAND \./generate_translations_header,COMMAND ${STAGING_DIR_NATIVE}/usr/bin/generate_translations_header,g' ${S}/translations/CMakeLists.txt
    # prevent memory blowup
    grep -q compile_job_pool ${S}/CMakeLists.txt || sed -i '/^project(monero)/a set_property(GLOBAL APPEND PROPERTY JOB_POOLS compile_job_pool=2)\nset_property(GLOBAL APPEND PROPERTY JOB_POOLS link_job_pool=2)\nset(CMAKE_JOB_POOL_COMPILE compile_job_pool)\nset(CMAKE_JOB_POOL_LINK link_job_pool)' ${S}/CMakeLists.txt
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
