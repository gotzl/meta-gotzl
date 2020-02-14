SUMMARY = ""
LICENSE = "LGPL"
LIC_FILES_CHKSUM = "file://COPYING;md5=fbc093901857fcd118f065f900982c24"

SRC_URI = "git://github.com/steve-o/openpgm.git;protocol=git;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/${PN}/pgm"

inherit autotools

do_configure() {
    ${S}/configure --build=x86_64-linux --host=aarch64-poky-linux --target=aarch64-poky-linux \
    --prefix=/usr --exec_prefix=/usr --bindir=/usr/bin --sbindir=/usr/sbin \
    --libexecdir=/usr/libexec --datadir=/usr/share --sysconfdir=/etc --sharedstatedir=/com \
    --localstatedir=/var --libdir=/usr/lib --includedir=/usr/include --oldincludedir=/usr/include \
    --infodir=/usr/share/info --mandir=/usr/share/man --disable-silent-rules \
    --disable-dependency-tracking \
    --with-libtool-sysroot=/opt/poky/build-rockpro64/tmp/work/aarch64-poky-linux/openpgm/1.0-r0/recipe-sysroot \
    --disable-static \
    ac_cv_file__proc_cpuinfo=yes \
    ac_cv_file__dev_rtc=no \
    ac_cv_file__dev_hpet=no \
    ac_fn_c_try_run=no \
    ac_cv_func_malloc_0_nonnull=yes \
    ac_cv_func_realloc_0_nonnull=yes \
    pgm_unaligned_pointers=yes 
}