require bitcoin.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=5ab93a32dcc9017fd179fb0dd92da0f0"
SRC_URI = "https://bitcoincore.org/bin/bitcoin-core-${PV}/bitcoin-${PV}.tar.gz"
SRC_URI[sha256sum] = "7da4ae1f78ab4f6c8b19c8ca89bd6b2a6c4836ea"


# starting with bitcoin>0.17.2, something broke the way how the 
# build system tries to find boost, so we force things...
EXTRA_OECONF += "\
	--with-boost=${STAGING_LIBDIR}/../include/boost/ \
	--with-boost-libdir=${STAGING_LIBDIR} \
"
