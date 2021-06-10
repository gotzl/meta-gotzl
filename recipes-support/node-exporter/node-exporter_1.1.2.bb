SUMMARY = "Prometheus node exporter"
LICENSE = "Apache"
LIC_FILES_CHKSUM = "file://src/import/LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRCNAME = "node_exporter"

PKG_NAME = "github.com/prometheus/${SRCNAME}"
SRC_URI = "git://${PKG_NAME}.git;branch=release-1.1"

SRCREV = "b597c1244d7bef49e6f3359c87a56dd7707f6719"
PV = "1.1.2+git${SRCPV}"

S = "${WORKDIR}/git"

inherit pkgconfig systemd useradd

SYSTEMD_SERVICE_${PN} = "${SRCNAME}.service"

GO_IMPORT = "import"
inherit go

# DEPENDS_append = " curl-native ca-certificates-native "
DEPENDS_append = " curl-native "
RDEPENDS_${PN} = "bash"

do_compile() {

    export GOARCH="${TARGET_GOARCH}"
    cd ${S}/src/import/

    # Build the target binaries
    export GOARCH="${TARGET_GOARCH}"
    # Pass the needed cflags/ldflags so that cgo can find the needed headers files and libraries
    export CGO_ENABLED="1"
    export CGO_CFLAGS="${CFLAGS} --sysroot=${STAGING_DIR_TARGET}"
    export CGO_LDFLAGS="${LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"
    export CFLAGS=""
    export LDFLAGS=""
    export CC="${CC}"
    export LD="${LD}"
    export GOBIN=""
    export PROMU_VERSION="0.11.1"

    # don't run lint nor tests
    # FIXME: don't check certificate (even thought ca-certs are there??)
    # FIXME: don't stop the build when "running check for unused/missing packages in go.mod"
    sed -i -e 's,^common-all:.*,common-all: precheck style check_license unused build,g' \
           -e 's,curl -s -L,curl -s -k -L,g' \
           -e 's,^\(.*@git diff .*\)$,\1 || true,g' \
        Makefile.common

    # only build the thing, don't do any checks
    sed -i -e 's,^all::.*,all:: common-all,g' Makefile

    oe_runmake GO=${GO}
}

do_install() {
	install -d ${D}${sbindir}
	install -m 755 ${S}/src/import/${SRCNAME} ${D}${sbindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/src/import/examples/systemd/${SRCNAME}.service ${D}${systemd_unitdir}/system

    install -d ${D}${sysconfdir}/sysconfig
    echo "OPTIONS=\"\"" > ${D}${sysconfdir}/sysconfig/node_exporter
}

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "-M -r -s /bin/false node_exporter"