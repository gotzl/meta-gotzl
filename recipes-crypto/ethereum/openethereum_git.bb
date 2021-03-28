inherit cargo systemd

SUMMARY = "parity-etherum"
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://../git/LICENSE;md5=7702f203b58979ebbc31bfaeb44f219c"

SRC_URI = "git://github.com/${PN}/${PN}.git;protocol=git;branch=main"
SRCREV = "5fdedf0858bf1ae95e718909a1476d8172845f6c"

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

EXTRA_USERS_PARAMS = "\
    groupadd ether; \
	useradd -u 1101 -g 1101 -M -r -s /bin/false ether; \
"
