SUMMARY = "Bitcoin"
LICENSE = "MIT"

DEPENDS = "boost libevent util-linux-native"
RDEPENDS_${PN} = "boost-system boost-thread boost-filesystem boost-chrono"

inherit autotools pkgconfig systemd useradd

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
           -e 's,^ExecStartPre=,# ExecStartPre=,g' \
           -e 's,^ConfigurationDirectory,# ConfigurationDirectory,g' \
        ${D}${systemd_unitdir}/system/bitcoind.service

    # FIXME:
    # comment ConfigurationDirectory, it conflicts with r/o filesystem and we don't use a config anyways
    # comment ExecStartPre=/bin/chgrp, it conflicts with r/o filesystem
}

SYSTEMD_SERVICE_${PN} = "bitcoind.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "-g 1100 bitcoin"
USERADD_PARAM_${PN} = "-u 1100 -g bitcoin -M -r -s /bin/false bitcoin"
