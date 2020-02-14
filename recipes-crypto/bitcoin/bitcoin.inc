SUMMARY = "Bitcoin"
LICENSE = "MIT"

DEPENDS = "boost libevent util-linux-native"
RDEPENDS_${PN} = "boost-system boost-thread boost-filesystem boost-chrono"

inherit autotools pkgconfig systemd

S = "${WORKDIR}/git"

EXTRA_OECONF += "\
	--with-boost-libdir=${STAGING_LIBDIR} \
"

PACKAGECONFIG ??= ""
PACKAGECONFIG[wallet] = "--enable-wallet,--disable-wallet, libdb_cxx"
PACKAGECONFIG[miniupnpc] = "--enable-miniupnpc,--disable-miniupnpc"
PACKAGECONFIG[gui] = "--enable-gui,--disable-gui"

do_install_append () {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/../bitcoind.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE_${PN} = "bitcoind.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"