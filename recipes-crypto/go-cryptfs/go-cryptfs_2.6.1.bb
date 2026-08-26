SUMMARY = "Encrypted overlay filesystem written in Go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=6fee026f0b48abb4d7cd72e25032503c"

GO_IMPORT = "github.com/rfjakob/gocryptfs"

SRC_URI = "git://${GO_IMPORT};protocol=https;destsuffix=${GO_SRCURI_DESTSUFFIX};branch=master"
SRCREV = "25e85a4454e93643ad92397d01ed532a360d7ee2"

B = "${S}/src/${GO_IMPORT}/bin"

inherit pkgconfig

DEPENDS = "openssl"
RDEPENDS:${PN} = "libcrypto fuse-utils bash"


do_compile:prepend() {
    # don't try to run the binary
    sed -i 's,^\(\./gocryptfs\),# \1,g' build.bash
    # don't render man pages
    sed -i 's,^\(render\s\),# \1,g' Documentation/MANPAGE-render.bash
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
	install -d ${D}${bindir}
	install -m 755 ${B}/${GO_BUILD_BINDIR}/* ${D}${bindir}
}

FILES:${PN} += "/usr/local/bin"
