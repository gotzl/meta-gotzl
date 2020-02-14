SUMMARY = ""
LICENSE = "monero-project"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f8e1a3a2c04c83ac016f899fe0287dcd"

SRC_URI = "git://github.com/monero-project/monero.git;protocol=git;tag=v${PV};nobranch=1"

S = "${WORKDIR}/git"
DEPENDS = "boost openssl zeromq openpgm unbound"

inherit cmake

EXTRA_OECMAKE="-D BUILD_TESTS=OFF -D CMAKE_BUILD_TYPE=release"

do_configure_prepend() {
    cd ${S}
    git submodule update --init --recursive
}


# do_compile() {
#     cd ${S}
#     make depends target=aarch64-linux-gnu
# }