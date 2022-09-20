SUMMARY = "Ethereum implementation on the efficiency frontier"
LICENSE = " LGPL-3.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

SRCNAME = "erigon"

PKG_NAME = "github.com/ledgerwatch/${SRCNAME}"
SRC_URI = "git://${PKG_NAME}.git;protocol=https;branch=alpha \
    file://erigon.service \
"

SRCREV = "32bd69e5316050005e34448ec6b0165f97173d50"

S = "${WORKDIR}/git"

inherit pkgconfig systemd

SYSTEMD_SERVICE:${PN} = "${PN}.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

GO_IMPORT = "github.com/ledgerwatch/erigon"

inherit go-mod

GOBUILDFLAGS:append = " -tags nosqlite"
export CGO_CFLAGS = "${CFLAGS} -DMDBX_FORCE_ASSERTIONS=0 -Wno-format-security -Wno-error=date-time"

do_compile() {
    oe_runmake GO=${GO} ${PN}
}
# need to download go modules
do_compile[network] = "1"


do_install() {
    install -d ${D}${bindir}
    install -m 755 ${B}/src/${GO_IMPORT}/build/bin/${SRCNAME} ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/${PN}.service ${D}${systemd_unitdir}/system    
}


FILES:${PN} = " \
    ${bindir}/${PN} \
    ${sysconfdir}/${PN}/${PN}.conf \
    ${systemd_unitdir}/system/${PN}.service \
"

REMOVE_LIBTOOL_LA = "0"
