SUMMARY = "Encrypted overlay filesystem written in Go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/import/LICENSE;md5=6fee026f0b48abb4d7cd72e25032503c"

SRCNAME = "gocryptfs"

PKG_NAME = "github.com/rfjakob/${SRCNAME}"
SRC_URI = "git://${PKG_NAME}.git"

SRCREV = "a6f515008f59131771581d084bcaca20e46e9e52"
PV = "1.8.0+git${SRCPV}"

S = "${WORKDIR}/git"

inherit pkgconfig

GO_IMPORT = "import"
inherit go

DEPENDS = "openssl"
RDEPENDS:${PN} = "libcrypto fuse-utils bash"

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
    export GO111MODULE=off

	# don't try to run the binary
	sed -i 's,^\(\./gocryptfs\),# \1,g' build.bash

	# don't render man pages
	sed -i 's,^\(render\),# \1,g' Documentation/MANPAGE-render.bash

    oe_runmake GO=${GO}
}


do_install() {
	install -d ${D}${bindir}
	install -m 755 ${B}/src/import/${SRCNAME} ${D}${bindir}
}