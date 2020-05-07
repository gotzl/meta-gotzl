SUMMARY = ""
LICENSE = "monero-project"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f8e1a3a2c04c83ac016f899fe0287dcd"

SRC_URI = "git://github.com/monero-project/monero.git;protocol=git;tag=v${PV};nobranch=1"

S = "${WORKDIR}/git"
DEPENDS = "boost openssl zeromq openpgm unbound"

# Workaround for high memory usage during build
PARALLEL_MAKE = ""

inherit cmake

EXTRA_OECMAKE=" \
    -D BUILD_DOCUMENTATION=OFF \
    -D BUILD_TESTS=OFF \
    -D CMAKE_BUILD_TYPE=release \
"

do_configure_prepend() {
    cd ${S}
    git submodule update --init --recursive

    # FIXME: this is a very crude hack to get the translation binary build for the host
    sed -i '/project(translations)/ a set(CMAKE_C_COMPILER /usr/bin/gcc)\
set(CMAKE_CXX_COMPILER /usr/bin/g++)\
set(CMAKE_C_FLAGS \"-march=native\")\
set(CMAKE_CXX_FLAGS \"-march=native\")\
set(CMAKE_SYSROOT \"/\")\
' translations/CMakeLists.txt
}
