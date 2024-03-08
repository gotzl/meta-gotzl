SRCREV_bitcoincore-rpc = "a96d245e4bd740bdbd1173c8ead1da47070e92a4"
SRCREV_bitcoincore-rpc-json = "a96d245e4bd740bdbd1173c8ead1da47070e92a4"

SRC_URI:append = " \
    file://serde_yaml.patch \
    file://bitcoin-exporter.conf \
    file://bitcoin-exporter.service \
"

inherit systemd rust-common cargo

SYSTEMD_SERVICE:${PN} = "bitcoin-exporter.service"

# need to update some git repo ...
# do_compile[network] = "1"

# FIXME: replace this by CARGO_NO_FROZEN in future 
CARGO_BUILD_FLAGS = "-v --offline --target ${RUST_HOST_SYS} ${BUILD_MODE} --manifest-path=${MANIFEST_PATH}"

do_install:append() {
    install -d ${D}${bindir}
    install -m 755 ${WORKDIR}/build/target/${TARGET_SYS}-gnu/release/bitcoin-exporter ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/bitcoin-exporter.service ${D}${systemd_unitdir}/system

    install -d ${D}${sysconfdir}/bitcoin
    install -m 644 ${WORKDIR}/bitcoin-exporter.conf ${D}${sysconfdir}/bitcoin/    
}
