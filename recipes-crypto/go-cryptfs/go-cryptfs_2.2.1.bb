SUMMARY = "Encrypted overlay filesystem written in Go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/import/LICENSE;md5=6fee026f0b48abb4d7cd72e25032503c"

SRCNAME = "gocryptfs"

PKG_NAME = "github.com/rfjakob/${SRCNAME}"
SRC_URI = "git://${PKG_NAME}.git;protocol=https;branch=master"
SRCREV = "4ba0ced3c704c1cc8696ea76d96822efdd1c7157"

S = "${WORKDIR}/git"

inherit pkgconfig

GO_IMPORT = "import"
inherit go-mod

DEPENDS = "openssl"
RDEPENDS:${PN} = "libcrypto fuse-utils bash"


do_compile:prepend() {
    pushd ${S}/src/import/

	# don't try to run the binary
	sed -i 's,^\(\./gocryptfs\),# \1,g' build.bash

	# don't render man pages
	sed -i 's,^\(render\s\),# \1,g' Documentation/MANPAGE-render.bash
    popd
}
# need to download go modules
do_compile[network] = "1"


do_install() {
	install -d ${D}${bindir}
	install -m 755 ${B}/${GO_BUILD_BINDIR}/* ${D}${bindir}
}