require bitcoin.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=889c1546f2fbc7c25083eb07eef8b289"
SRC_URI = "https://bitcoincore.org/bin/bitcoin-core-${PV}/bitcoin-${PV}.tar.gz"
SRC_URI[sha256sum] = "d0e9d089b57048b1555efa7cd5a63a7ed042482045f6f33402b1df425bf9613b"


# starting with bitcoin>0.17.2, something broke the way how the 
# build system tries to find boost, so we force things...
EXTRA_OECONF += "\
	--with-boost=${STAGING_LIBDIR}/../include/boost/ \
	--with-boost-libdir=${STAGING_LIBDIR} \
"
