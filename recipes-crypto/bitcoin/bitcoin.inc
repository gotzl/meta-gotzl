SUMMARY = "Bitcoin"
LICENSE = "MIT"

DEPENDS = "boost libevent util-linux-native"
RDEPENDS_${PN} = "boost-system boost-thread boost-filesystem boost-chrono"

inherit autotools pkgconfig systemd extrausers

S = "${WORKDIR}/git"

PACKAGECONFIG ??= ""
PACKAGECONFIG[wallet] = "--enable-wallet,--disable-wallet, libdb_cxx"
PACKAGECONFIG[miniupnpc] = "--enable-miniupnpc,--disable-miniupnpc"
PACKAGECONFIG[gui] = "--enable-gui,--disable-gui"

do_install_append () {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/contrib/init/bitcoind.service ${D}${systemd_unitdir}/system

    # adjust startup parameters
    sed -i -e 's,\(^ExecStart=/usr/bin/bitcoind\),\1 -disablewallet -printtoconsole -dbcache=100,g' \
        ${D}${systemd_unitdir}/system/bitcoind.service
}

SYSTEMD_SERVICE_${PN} = "bitcoind.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

EXTRA_USERS_PARAMS = "\
	useradd -M -r -s /bin/flase bitcoin \
"