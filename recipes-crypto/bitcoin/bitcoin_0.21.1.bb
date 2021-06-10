require bitcoin.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=b544c0e0cfe77a2081989a15a9b9b4f6"
SRC_URI = "git://github.com/bitcoin/bitcoin.git;protocol=git;tag=v${PV};nobranch=1"

# starting with bitcoin>0.17.2, something broke the way how the 
# build system tries to find boost, so we force things...
EXTRA_OECONF += "\
	--with-boost=${STAGING_LIBDIR}/../include/boost/ \
	--with-boost-libdir=${STAGING_LIBDIR} \
"
