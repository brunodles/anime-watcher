# Cache
This folder provides cached pages for our tests.
With it we don't need to fetch pages every time.

The cache is also useful to look the real page content, as it will not execute
`javascript` nor `iframes`. Some pages also have different responses depending
on `user agent` used to fetch the page.

This cache cam be disabled during build time.
