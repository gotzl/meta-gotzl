SUMMARY = "Ethereum implementation on the efficiency frontier"
LICENSE = " LGPL-3.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

SRCNAME = "erigon"

PKG_NAME = "github.com/ledgerwatch/${SRCNAME}"
SRC_URI = "git://${PKG_NAME}.git;branch=stable \
    file://erigon.service \
    file://erigon-rpcdaemon.service \
"

SRCREV = "v${PV}"

S = "${WORKDIR}/git"

inherit pkgconfig systemd

SYSTEMD_PACKAGES = "${PN} ${PN}-rpcdaemon"
SYSTEMD_SERVICE_${PN} = "${PN}.service"
SYSTEMD_SERVICE_${PN}-rpcdaemon = "${PN}-rpcdaemon.service"

SYSTEMD_AUTO_ENABLE_${PN} = "disable"
SYSTEMD_AUTO_ENABLE_${PN}-rpcdaemon = "disable"

PACKAGE_BEFORE_PN = "${PN}-rpcdaemon"
RDEPENDS_${PN} += "${PN}-rpcdaemon"


GO_IMPORT = "github.com/ledgerwatch/erigon"
inherit go


do_compile() {

    export GOARCH="${TARGET_GOARCH}"
    cd ${S}/src/${GO_IMPORT}/

    # Build the target binaries
    export GOARCH="${TARGET_GOARCH}"
    # Pass the needed cflags/ldflags so that cgo can find the needed headers files and libraries
    export CGO_ENABLED="1"
    export CGO_CFLAGS="${CFLAGS} -Wno-format-security --sysroot=${STAGING_DIR_TARGET}"
    export CGO_LDFLAGS="${LDFLAGS} --sysroot=${STAGING_DIR_TARGET}"
    export CFLAGS=""
    export LDFLAGS=""
    export CC="${CC}"
    export LD="${LD}"
    export GOBIN=""
    # export GO111MODULE=off
    export GOFLAGS=-modcacherw

    oe_runmake GO=${GO} ${PN}
    oe_runmake GO=${GO} rpcdaemon
}


do_install() {
	install -d ${D}${bindir}
	install -m 755 ${B}/src/${GO_IMPORT}/build/bin/${SRCNAME} ${D}${bindir}
	install -m 755 ${B}/src/${GO_IMPORT}/build/bin/rpcdaemon ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${PN}.service ${D}${systemd_unitdir}/system    
    install -m 0644 ${WORKDIR}/${PN}-rpcdaemon.service ${D}${systemd_unitdir}/system    
}


FILES_${PN} = " \
	${bindir}/${PN} \
	${sysconfdir}/${PN}/${PN}.conf \
        ${systemd_unitdir}/system/${PN}.service \
"

FILES_${PN}-rpcdaemon = " \
        ${bindir}/rpcdaemon \
        ${sysconfdir}/${PN}/rpcdaemon.conf \
        ${systemd_unitdir}/system/rpcdaemon.service \
"
REMOVE_LIBTOOL_LA = "0"
