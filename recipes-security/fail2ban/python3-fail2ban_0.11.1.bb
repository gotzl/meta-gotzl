# inspired by the recipe in https://layers.openembedded.org/layerindex/branch/master/layer/meta-security/
SUMMARY = "Daemon to ban hosts that cause multiple authentication errors."
DESCRIPTION = "Fail2Ban scans log files like /var/log/auth.log and bans IP addresses having too \
many failed login attempts. It does this by updating system firewall rules to reject new \
connections from those IP addresses, for a configurable amount of time. Fail2Ban comes \
out-of-the-box ready to read many standard log files, such as those for sshd and Apache, \
and is easy to configure to read any log file you choose, for any error you choose."
HOMEPAGE = "http://www.fail2ban.org"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=ecabc31e90311da843753ba772885d9f"

SRCREV ="d004a2c79b76b7dcfb2092bab3dc53840fddf558"
SRC_URI = " git://github.com/fail2ban/fail2ban.git;branch=master \
    file://jail.local \
"

inherit setuptools3 systemd

S = "${WORKDIR}/git"

do_compile_append () {
    2to3 -w -n ${S}/build
}

do_install_append () {
    install -d ${D}/${sysconfdir}/fail2ban
    install -d ${D}${systemd_unitdir}/system/
    install -m 644 ${WORKDIR}/jail.local ${D}/${sysconfdir}/fail2ban/
    install -m 644 ${S}/files/fail2ban.service.in ${D}${systemd_unitdir}/system/fail2ban.service
    sed -i 's\@BINDIR@\'${bindir}'\g' ${D}${systemd_unitdir}/system/fail2ban.service

    # the fail2ban setup script creates a link to the current python binary, 
    # which picks up the hosts python binary.. so we have to correct the link
    rm ${D}${bindir}/fail2ban-python
    ln -s python3 ${D}${bindir}/fail2ban-python

    chown -R root:root ${D}/${bindir}
}

FILES_${PN} += "/run"

SYSTEMD_SERVICE_${PN} = "fail2ban.service"

INSANE_SKIP_${PN}_append = "already-stripped"

RDEPENDS_${PN} = "iptables sqlite3 ${PYTHON_PN}-pyinotify"

# __prefix_line = %(__date_ambit)s?\s*(?:%(__bsd_syslog_verbose)s\s+)?(?:%(__hostname)s\s+)?(?:%(__kernel_prefix)s\s+)?(?:%(__vserver)s\s+)?(?:%(__daemon_combs_re)s\s+)?(?:%(__daemon_extra_re)s\s+)?
# __prefix_line = %(__date_ambit)s?\s*(?:%(__bsd_syslog_verbose)s\s+)?(?:%(__hostname)s\s+)?(?:%(__kernel_prefix)s\s+)?(?:%(__vserver)s\s+)?(auth.info\s+)?(?:%(__daemon_combs_re)s\s+)?(?:%(__daemon_extra_re)s\s+)?

