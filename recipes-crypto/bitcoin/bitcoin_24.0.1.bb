require bitcoin.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=d63289bf862229a0ba143ebeadb987a3"
SRC_URI = "https://bitcoincore.org/bin/bitcoin-core-${PV}/bitcoin-${PV}.tar.gz"
SRC_URI[sha256sum] = "12d4ad6dfab4767d460d73307e56d13c72997e114fad4f274650f95560f5f2ff"


# starting with bitcoin>0.17.2, something broke the way how the 
# build system tries to find boost, so we force things...
EXTRA_OECONF += "\
	--with-boost=${STAGING_LIBDIR}/../include/boost/ \
	--with-boost-libdir=${STAGING_LIBDIR} \
"
