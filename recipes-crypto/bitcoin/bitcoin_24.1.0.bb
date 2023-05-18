require bitcoin.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=d63289bf862229a0ba143ebeadb987a3"
SRC_URI = "https://bitcoincore.org/bin/bitcoin-core-${PV}/bitcoin-${PV}.tar.gz"
SRC_URI[sha256sum] = "8a0a3db3b2d9cc024e897113f70a3a65d8de831c129eb6d1e26ffa65e7bfaf4e"


# starting with bitcoin>0.17.2, something broke the way how the 
# build system tries to find boost, so we force things...
EXTRA_OECONF += "\
	--with-boost=${STAGING_LIBDIR}/../include/boost/ \
	--with-boost-libdir=${STAGING_LIBDIR} \
"
