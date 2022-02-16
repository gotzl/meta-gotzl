
do_install:append () {
    # ignore the dummy0 device
	if ${@bb.utils.contains('PACKAGECONFIG','networkd','true','false',d)}; then
        sed -i 's@/lib/systemd/systemd-networkd-wait-online@/lib/systemd/systemd-networkd-wait-online --ignore=dummy0@g' ${D}${systemd_unitdir}/system/systemd-networkd-wait-online.service
	fi

    # mask auto configuration of wired networks, it interferes with docker veth devices
    ln -s /dev/null ${D}${sysconfdir}/systemd/network/80-wired.network
}
