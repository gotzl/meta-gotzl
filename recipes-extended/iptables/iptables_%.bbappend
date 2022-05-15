PACKAGECONFIG:append = " libnftnl legacy"
PACKAGECONFIG[legacy] = ""
DEPENDS:append = " bison-native"

do_install:append() {
    apps="arptables ebtables iptables"
    if ${@bb.utils.contains("DISTRO_FEATURES", "ipv6", 'true','false', d)}; then
        apps="${apps} ip6tables"
    fi

    for i in $apps; do
        mv ${D}/usr/sbin/${i} ${D}/usr/sbin/${i}-legacy
        if ${@bb.utils.contains('PACKAGECONFIG', 'legacy', 'true','false', d)}; then
            ln -s ${i}-legacy ${D}/usr/sbin/${i}
        else
            ln -s ${i}-nft ${D}/usr/sbin/${i}
        fi        
    done

    for i in restore save; do
        rm ${D}/usr/sbin/iptables-${i}
        if ${@bb.utils.contains('PACKAGECONFIG', 'legacy', 'true','false', d)}; then
            ln -s iptables-legacy-${i} ${D}/usr/sbin/iptables-${i}
        else
            ln -s iptables-nft-${i} ${D}/usr/sbin/iptables-${i}
        fi

        if ${@bb.utils.contains("DISTRO_FEATURES", "ipv6", 'true','false', d)}; then
            rm ${D}/usr/sbin/ip6tables-${i}
            if ${@bb.utils.contains('PACKAGECONFIG', 'legacy', 'true','false', d)}; then
                ln -s ip6tables-legacy-${i} ${D}/usr/sbin/ip6tables-${i}
            else
                ln -s ip6tables-nft-${i} ${D}/usr/sbin/ip6tables-${i}
            fi
        fi
    done
}