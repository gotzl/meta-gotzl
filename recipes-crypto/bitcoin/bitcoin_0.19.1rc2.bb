require bitcoin.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=1320175568754b5b3191d5b3e8bd986b"
SRC_URI = "git://github.com/bitcoin/bitcoin.git;protocol=git;tag=v${PV};nobranch=1 \
	file://bitcoind.service \
"

# starting with bitcoin>0.17.2, something broke the way how the 
# build system tries to find boost, so we force things...
EXTRA_OECONF += "\
	--with-boost=${STAGING_LIBDIR}/../include/boost/ \
	--with-boost-libdir=${STAGING_LIBDIR} \
"