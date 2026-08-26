SUMMARY = "A small python module for wake on lan."
HOMEPAGE = "https://github.com/remcohaszing/pywakeonlan"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=f8b3f077e531182116789a1d25297f55"

SRC_URI[sha256sum] = "1cb30c4ae60f85ef289f4f7d4f703382ed19ba84103206dc5e2b1e51bec9f456"

S = "${UNPACKDIR}/wakeonlan-${PV}"

inherit pypi python_setuptools_build_meta

DEPENDS = "python3-poetry-core-native"

RDEPENDS_${PN} += "python3-core python3-io python3-pkg-resources"
