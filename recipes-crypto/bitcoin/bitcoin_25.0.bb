require bitcoin.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=5ab93a32dcc9017fd179fb0dd92da0f0"
SRC_URI = "https://bitcoincore.org/bin/bitcoin-core-${PV}/bitcoin-${PV}.tar.gz"
SRC_URI[sha256sum] = "5df67cf42ca3b9a0c38cdafec5bbb517da5b58d251f32c8d2a47511f9be1ebc2"


# starting with bitcoin>0.17.2, something broke the way how the 
# build system tries to find boost, so we force things...
EXTRA_OECONF += "\
	--with-boost=${STAGING_LIBDIR}/../include/boost/ \
	--with-boost-libdir=${STAGING_LIBDIR} \
"
