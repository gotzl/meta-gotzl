inherit cargo systemd useradd

SUMMARY = "parity-etherum"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://../git/LICENSE;md5=7702f203b58979ebbc31bfaeb44f219c"

SRC_URI = "git://github.com/${BPN}/${BPN}.git;protocol=git;branch=main"
SRCREV = "f9f492638cce143efc5106c1bdf6f4dba5cdff8b"

S = "${WORKDIR}/git"

DEPENDS = "cmake-native"
CARGO_FEATURES = "final"

do_install_append () {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/scripts/openethereum.service ${D}${systemd_unitdir}/system

    sed -i -e 's,^# User=username,User=ether,g' \
           -e 's,^# Group=groupname,Group=ether,g' \
        ${D}${systemd_unitdir}/system/openethereum.service
}

SYSTEMD_SERVICE_${PN} = "openethereum.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "-g 1101 ether"
USERADD_PARAM_${PN} = "-u 1101 -g ether -M -r -s /bin/false ether"
