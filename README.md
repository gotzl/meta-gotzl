# meta-gotzl

This README file contains information on the contents of the meta-gotzl layer.
The layer contains mainly recipes for building crypto currency clients. So far, these are [bitcoin](https://github.com/bitcoin/bitcoin), [monero](https://github.com/monero-project/monero), [openethereum](https://github.com/openethereum/openethereum).
It is used in my [ROCKPro64-image](https://github.com/gotzl/meta-gotzl-rk64) but the recipes could be interesting for others as well.
Please see the corresponding sections below for details.

## Dependencies

This layer depends on:

[poky](git://git.yoctoproject.org/poky)
* branch: dunfell

[meta-openembedded](git://git.openembedded.org/meta-openembedded)
* layers: meta-oe meta-filesystems meta-networking
* branch: dunfell

[meta-python]()
* branch dunfell

[meta-rust-bin](https://github.com/rust-embedded/meta-rust-bin)
(pulled in by OpenEthereum)
* branch master
