# meta-gotzl

This README file contains information on the contents of the meta-gotzl layer.
The layer is used in my [ROCKPro64-image](https://github.com/gotzl/meta-gotzl-rk64) but contains recipes that could be interesting for others as well.
Please see the corresponding sections below for details.

## Dependencies

This layer depends on:

[poky](git://git.yoctoproject.org/poky)
* branch: zeus

[meta-openembedded](git://git.openembedded.org/meta-openembedded)
* layers: meta-oe meta-python
* branch: zeus

## Notes

### monero
Does not build.

### docker-compose
For `docker-compose` to build, the recipes in `recipes-devtools/python` are needed. Select these versions in your image with
```
PREFERRED_VERSION_python3-jsonschema = "2.6.0" 
PREFERRED_VERSION_python3-requests = "2.19.1" 
PREFERRED_VERSION_python3-requests = "3.13" 
PREFERRED_VERSION_python3-urllib3 = "1.23" 
PREFERRED_VERSION_python3-idna = "2.7" 
```