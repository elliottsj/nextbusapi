nextbusapi [![Travis CI][travis-badge]][travis] [![Bintray][bintray-badge]][bintray]
==========

A RESTful web service adapter for the NextBus prediction and transit network reporting service, compatible with Android.

This project is a fork of the [NextBus Public Feed Adapter for Java](http://sourceforge.net/projects/nextbusapi/) by 
[Jim Doyle](http://jim_doyle.users.sourceforge.net/).

### Goals

- Make working with the NextBus API easier on the Android platform, without the need for a proxy server.
- Maintain the same existing functionality of Jim Doyle's *NextBus Public Feed Adapter for Java*.

### Notes

- Jim Doyle's library relies on [JAXB](http://en.wikipedia.org/wiki/Java_Architecture_for_XML_Binding), 
  which is not available on Android, so this library uses [SimpleXML](http://simple.sourceforge.net/home.php) instead.

[travis-badge]:  https://img.shields.io/travis/elliottsj/nextbusapi.svg?style=flat
[travis]:        https://travis-ci.org/elliottsj/nextbusapi
[bintray-badge]: https://api.bintray.com/packages/elliottsj/maven/nextbusapi/images/download.svg
[bintray]:       https://bintray.com/elliottsj/maven/nextbusapi/_latestVersion
