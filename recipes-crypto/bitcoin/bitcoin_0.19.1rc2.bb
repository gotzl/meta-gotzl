require bitcoin.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=1320175568754b5b3191d5b3e8bd986b"
SRC_URI = "git://github.com/bitcoin/bitcoin.git;protocol=git;tag=v${PV};nobranch=1 \
	file://bitcoind.service \
"