require bitcoin.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=5ab93a32dcc9017fd179fb0dd92da0f0"
SRC_URI = "https://bitcoincore.org/bin/bitcoin-core-${PV}/bitcoin-${PV}.tar.gz"
SRC_URI[sha256sum] = "bec2a598d8dfa8c2365b77f13012a733ec84b8c30386343b7ac1996e901198c9"


# starting with bitcoin>0.17.2, something broke the way how the 
# build system tries to find boost, so we force things...
EXTRA_OECONF += "\
	--with-boost=${STAGING_LIBDIR}/../include/boost/ \
	--with-boost-libdir=${STAGING_LIBDIR} \
"
