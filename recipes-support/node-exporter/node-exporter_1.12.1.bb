SUMMARY = "Prometheus node exporter"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

GO_IMPORT = "github.com/prometheus/node_exporter"

SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${GO_SRCURI_DESTSUFFIX};branch=release-1.12"
SRCREV = "6044da783597cc3b57aef7580ddcdcff58a4ee99"

B = "${S}/src/${GO_IMPORT}/bin"

inherit pkgconfig systemd useradd

SYSTEMD_SERVICE:${PN} = "node_exporter.service"

DEPENDS:append = " curl-native "
RDEPENDS:${PN} = "bash"


do_compile:prepend() {
    # don't run lint nor tests
    # FIXME: don't check certificate (even thought ca-certs are there??)
    # FIXME: don't stop the build when "running check for unused/missing packages in go.mod"
    sed -i -e 's,^common-all:.*,common-all: precheck style check_license unused build,g' \
           -e 's,curl -s -L,curl -s -k -L,g' \
           -e 's,^\(.*@git diff .*\)$,\1 || true,g' \
        Makefile.common

    # only build the thing, don't do any checks
    sed -i -e 's,^all::.*,all:: common-all,g' Makefile
}
# need to download go modules
do_compile[network] = "1"


GO_INSTALL = "${GO_IMPORT}/v2"

# build executable instead of shared object
GO_LINKSHARED = ""
# -buildmode=pie requires external (cgo) linking on ARM and x86
GOBUILDFLAGS:remove = "-buildmode=pie"

inherit go-mod


do_install() {
    install -d ${D}${sbindir}
    install -m 755 ${B}/${GO_BUILD_BINDIR}/* ${D}${sbindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/src/${GO_IMPORT}/examples/systemd/node_exporter.service ${D}${systemd_unitdir}/system

    install -d ${D}${sysconfdir}/sysconfig
    echo "OPTIONS=\"\"" > ${D}${sysconfdir}/sysconfig/node_exporter
}

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-M -r -s /bin/false node_exporter"
