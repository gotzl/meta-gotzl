SUMMARY = "Prometheus node exporter"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/import/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRCNAME = "node_exporter"

PKG_NAME = "github.com/prometheus/${SRCNAME}"
SRC_URI = "git://${PKG_NAME}.git;protocol=https;branch=release-1.3"
SRCREV = "a2321e7b940ddcff26873612bccdf7cd4c42b6b6"

S = "${WORKDIR}/git"

inherit pkgconfig systemd useradd

SYSTEMD_SERVICE:${PN} = "${SRCNAME}.service"

GO_IMPORT = "import"

inherit go-mod

DEPENDS:append = " curl-native "
RDEPENDS:${PN} = "bash"


do_compile:prepend() {
    pushd ${S}/src/import/

    # don't run lint nor tests
    # FIXME: don't check certificate (even thought ca-certs are there??)
    # FIXME: don't stop the build when "running check for unused/missing packages in go.mod"
    sed -i -e 's,^common-all:.*,common-all: precheck style check_license unused build,g' \
           -e 's,curl -s -L,curl -s -k -L,g' \
           -e 's,^\(.*@git diff .*\)$,\1 || true,g' \
        Makefile.common

    # only build the thing, don't do any checks
    sed -i -e 's,^all::.*,all:: common-all,g' Makefile
    popd
}
# need to download go modules
do_compile[network] = "1"


do_install() {
	install -d ${D}${sbindir}
	install -m 755 ${B}/${GO_BUILD_BINDIR}/* ${D}${sbindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/src/import/examples/systemd/${SRCNAME}.service ${D}${systemd_unitdir}/system

    install -d ${D}${sysconfdir}/sysconfig
    echo "OPTIONS=\"\"" > ${D}${sysconfdir}/sysconfig/node_exporter
}

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-M -r -s /bin/false node_exporter"
