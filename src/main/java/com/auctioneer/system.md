
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="robots" content="noodp" />
        <title>Live Comment System Design - System Design</title><meta name="Description" content="building the live commenting real-time platform"><meta property="og:title" content="Live Comment System Design" />
<meta property="og:description" content="building the live commenting real-time platform" />
<meta property="og:type" content="article" />
<meta property="og:url" content="https://systemdesign.one/live-comment-system-design/" /><meta property="og:image" content="https://systemdesign.one/live-comment-system-design/live-comment.webp" /><meta property="article:section" content="posts" />
<meta property="article:published_time" content="2023-04-08T00:00:00+00:00" />
<meta property="article:modified_time" content="2023-05-18T00:00:00+00:00" /><meta property="og:site_name" content="System Design" />

<meta name="twitter:card" content="summary_large_image"/>
<meta name="twitter:image" content="https://systemdesign.one/live-comment-system-design/live-comment.webp"/>

<meta name="twitter:title" content="Live Comment System Design"/>
<meta name="twitter:description" content="building the live commenting real-time platform"/>
<meta name="application-name" content="System Design">
<meta name="apple-mobile-web-app-title" content="System Design"><meta name="theme-color" content="#ffffff"><meta name="msapplication-TileColor" content="#da532c"><link rel="shortcut icon" type="image/x-icon" href="/favicon.ico" />
        <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png"><link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png"><link rel="mask-icon" href="/safari-pinned-tab.svg" color="#5bbad5"><link rel="manifest" href="/site.webmanifest"><link rel="canonical" href="https://systemdesign.one/live-comment-system-design/" /><link rel="prev" href="https://systemdesign.one/leaderboard-system-design/" /><link rel="next" href="https://systemdesign.one/real-time-presence-platform-system-design/" /><link rel="stylesheet" href="/css/style.min.css"><link rel="preload" href="/lib/fontawesome-free/all.min.css" as="style" onload="this.onload=null;this.rel='stylesheet'">
        <noscript><link rel="stylesheet" href="/lib/fontawesome-free/all.min.css"></noscript><link rel="preload" href="/lib/animate/animate.min.css" as="style" onload="this.onload=null;this.rel='stylesheet'">
        <noscript><link rel="stylesheet" href="/lib/animate/animate.min.css"></noscript><meta name="msvalidate.01" content="FD39F28F92F811F282452AC196AB113C" /><meta name="yandex-verification" content="b2096c1f054386e1" /><script type="application/ld+json">
    {
        "@context": "http://schema.org",
        "@type": "BlogPosting",
        "headline": "Live Comment System Design",
        "inLanguage": "en",
        "mainEntityOfPage": {
            "@type": "WebPage",
            "@id": "https:\/\/systemdesign.one\/live-comment-system-design\/"
        },"genre": "posts","wordcount":  9373 ,
        "url": "https:\/\/systemdesign.one\/live-comment-system-design\/","datePublished": "2023-04-08T00:00:00+00:00","dateModified": "2023-05-18T00:00:00+00:00","license": "CC BY-NC 4.0","publisher": {
            "@type": "Organization",
            "name": ""},"author": {
                "@type": "Person",
                "name": "Neo Kim"
            },"description": "building the live commenting real-time platform"
    }
    </script></head>
    <body data-header-desktop="fixed" data-header-mobile="auto"><script type="text/javascript">(window.localStorage && localStorage.getItem('theme') ? localStorage.getItem('theme') === 'dark' : ('light' === 'auto' ? window.matchMedia('(prefers-color-scheme: dark)').matches : 'light' === 'dark')) && document.body.setAttribute('theme', 'dark');</script>

        <div id="mask"></div><div class="wrapper"><header class="desktop" id="header-desktop">
    <div class="header-wrapper">
        <div class="header-title">
            <a href="/" title="System Design">System Design</a>
        </div>
        <div class="menu">
            <div class="menu-inner"><a class="menu-item" href="https://newsletter.systemdesign.one/subscribe" title="Newsletter" rel="noopener noreffer" target="_blank"> Join Newsletter </a><a class="menu-item" href="/categories/" title="Archive"> Archive </a><a class="menu-item" href="/about/" title="About"> About </a><span class="menu-item delimiter"></span><span class="menu-item search" id="search-desktop">
                        <input type="text" placeholder="Search titles or contents..." id="search-input-desktop">
                        <a href="javascript:void(0);" class="search-button search-toggle" id="search-toggle-desktop" title="Search">
                            <i class="fas fa-search fa-fw" aria-hidden="true"></i>
                        </a>
                        <a href="javascript:void(0);" class="search-button search-clear" id="search-clear-desktop" title="Clear">
                            <i class="fas fa-times-circle fa-fw" aria-hidden="true"></i>
                        </a>
                        <span class="search-button search-loading" id="search-loading-desktop">
                            <i class="fas fa-spinner fa-fw fa-spin" aria-hidden="true"></i>
                        </span>
                    </span><a href="javascript:void(0);" class="menu-item theme-switch" title="Switch Theme">
                    <i class="fas fa-adjust fa-fw" aria-hidden="true"></i>
                </a></div>
        </div>
    </div>
</header><header class="mobile" id="header-mobile">
    <div class="header-container">
        <div class="header-wrapper">
            <div class="header-title">
                <a href="/" title="System Design">System Design</a>
            </div>
            <div class="menu-toggle" id="menu-toggle-mobile">
                <span></span><span></span><span></span>
            </div>
        </div>
        <div class="menu" id="menu-mobile"><div class="search-wrapper">
                    <div class="search mobile" id="search-mobile">
                        <input type="text" placeholder="Search titles or contents..." id="search-input-mobile">
                        <a href="javascript:void(0);" class="search-button search-toggle" id="search-toggle-mobile" title="Search">
                            <i class="fas fa-search fa-fw" aria-hidden="true"></i>
                        </a>
                        <a href="javascript:void(0);" class="search-button search-clear" id="search-clear-mobile" title="Clear">
                            <i class="fas fa-times-circle fa-fw" aria-hidden="true"></i>
                        </a>
                        <span class="search-button search-loading" id="search-loading-mobile">
                            <i class="fas fa-spinner fa-fw fa-spin" aria-hidden="true"></i>
                        </span>
                    </div>
                    <a href="javascript:void(0);" class="search-cancel" id="search-cancel-mobile">
                        Cancel
                    </a>
                </div><a class="menu-item" href="https://newsletter.systemdesign.one/subscribe" title="Newsletter" rel="noopener noreffer" target="_blank">Join Newsletter</a><a class="menu-item" href="/categories/" title="Archive">Archive</a><a class="menu-item" href="/about/" title="About">About</a><a href="javascript:void(0);" class="menu-item theme-switch" title="Switch Theme">
                <i class="fas fa-adjust fa-fw" aria-hidden="true"></i>
            </a></div>
    </div>
</header><div class="search-dropdown desktop">
        <div id="search-dropdown-desktop"></div>
    </div>
    <div class="search-dropdown mobile">
        <div id="search-dropdown-mobile"></div>
    </div><main class="main">
                <div class="container"><div class="toc" id="toc-auto">
    <h2 class="toc-title">Contents</h2>
    <div class="toc-content" id="toc-content-auto"></div>
</div><article class="page single"><h1 class="single-title animate__animated animate__flipInX">Live Comment System Design</h1><h2 class="single-subtitle">Real-Time Live Commenting Platform</h2><div class="post-meta">
        <div class="post-meta-line"><span class="post-author"><a href="https://www.linkedin.com/in/nk-systemdesign-one/" title="Author" target="_blank" rel="noopener noreffer author" class="author"><i class="fas fa-user-circle fa-fw" aria-hidden="true"></i>Neo Kim</a></span>&nbsp;<span class="post-category">included in <a href="/categories/deep-dive/"><i class="far fa-folder fa-fw"
                    aria-hidden="true"></i>Deep Dive</a></span></div>
        <div class="post-meta-line"><i class="far fa-calendar-alt fa-fw" aria-hidden="true"></i>&nbsp;<time datetime="2023-04-08">2023-04-08</time>&nbsp;<i class="fas fa-pencil-alt fa-fw" aria-hidden="true"></i>&nbsp;9373 words&nbsp;
            <i class="far fa-clock fa-fw" aria-hidden="true"></i>&nbsp;45 minutes&nbsp;</div>
    </div><div class="featured-image"><img
        class="lazyload"
        src="/svg/loading.min.svg"
        data-src="/live-comment-system-design/live-comment.webp"
        data-srcset="/live-comment-system-design/live-comment.webp, /live-comment-system-design/live-comment.webp 1.5x, /live-comment-system-design/live-comment.webp 2x"
        data-sizes="auto"
        alt="/live-comment-system-design/live-comment.webp"
        title="building the live commenting real-time platform" width="1454" height="984" /></div><div class="details toc" id="toc-static" data-kept="">
        <div class="details-summary toc-title">
            <span>Contents</span>
            <span><i class="details-icon fas fa-angle-right" aria-hidden="true"></i></span>
        </div>
        <div class="details-content toc-content" id="toc-content-static"><nav id="TableOfContents">
  <ul>
    <li><a href="#how-does-the-live-commentwork">How Does the Live Comment Work?</a></li>
    <li><a href="#what-is-a-livevideo">What is a Live Video?</a></li>
    <li><a href="#what-are-live-comments">What are Live Comments?</a></li>
    <li><a href="#terminology">Terminology</a></li>
    <li><a href="#questions-to-ask-the-interviewer">Questions to ask the Interviewer</a>
      <ul>
        <li><a href="#candidate">Candidate</a></li>
        <li><a href="#interviewer">Interviewer</a></li>
      </ul>
    </li>
    <li><a href="#requirements">Requirements</a>
      <ul>
        <li><a href="#functional-requirements">Functional Requirements</a></li>
        <li><a href="#non-functional-requirements">Non-Functional Requirements</a></li>
      </ul>
    </li>
    <li><a href="#live-commenting-apidesign">Live Commenting API Design</a>
      <ul>
        <li><a href="#how-does-the-receiver-subscribe-to-a-specific-livevideo">How does the receiver subscribe to a specific live video?</a></li>
        <li><a href="#how-does-the-receiver-unsubscribe-from-a-livevideo">How does the receiver unsubscribe from a live video?</a></li>
        <li><a href="#how-does-the-client--publish-a-livecomment">How does the client  publish a live comment?</a></li>
      </ul>
    </li>
    <li><a href="#further-system-design-learning-resources">Further System Design Learning Resources</a></li>
    <li><a href="#live-comment-system-databasedesign">Live Comment System Database Design</a>
      <ul>
        <li><a href="#database-schemadesign">Database Schema Design</a>
          <ul>
            <li><a href="#comments-table">Comments table</a></li>
            <li><a href="#videos-table">Videos table</a></li>
            <li><a href="#users-table">Users table</a></li>
          </ul>
        </li>
        <li><a href="#sql">SQL</a>
          <ul>
            <li><a href="#write-a-sql-query-to-fetch-the-latest-ten-comments-on-the-live-video-with-35-as-the-video-id">Write a SQL query to fetch the latest ten comments on the live video with &ldquo;35&rdquo; as the video ID</a></li>
            <li><a href="#write-a-sql-query-to-insert-a-new-live-comment">Write a SQL query to insert a new live comment</a></li>
            <li><a href="#write-a-sql-query-to-fetch-the-total-count-of-comments-on-each-video">Write a SQL query to fetch the total count of comments on each video</a></li>
          </ul>
        </li>
        <li><a href="#type-of-datastore">Type of Data Store</a></li>
      </ul>
    </li>
    <li><a href="#capacity-planning">Capacity Planning</a>
      <ul>
        <li><a href="#traffic">Traffic</a></li>
        <li><a href="#storage">Storage</a></li>
        <li><a href="#bandwidth">Bandwidth</a></li>
        <li><a href="#memory">Memory</a></li>
      </ul>
    </li>
    <li><a href="#further-system-design-learning-resources-1">Further System Design Learning Resources</a></li>
    <li><a href="#live-commenting-high-level-design">Live Commenting High-Level Design</a>
      <ul>
        <li><a href="#write-globally-and-readlocally">Write Globally and Read Locally</a></li>
        <li><a href="#write-locally-and-readglobally">Write Locally and Read Globally</a></li>
        <li><a href="#prototyping-a-live-commentservice">Prototyping a Live Comment Service</a></li>
        <li><a href="#distribution-of-livecomments">Distribution of Live Comments</a></li>
        <li><a href="#live-commenting-with-pub-subserver">Live Commenting With Pub-Sub Server</a>
          <ul>
            <li><a href="#using-apache-kafka-as-the-pub-sub-server">Using Apache Kafka as the Pub-Sub Server</a></li>
            <li><a href="#using-redis-as-the-pub-sub-server">Using Redis as the Pub-Sub Server</a></li>
            <li><a href="#using-redis-streams-as-the-pub-sub-server">Using Redis Streams as the Pub-Sub Server</a></li>
          </ul>
        </li>
        <li><a href="#an-abstract-real-time-platform">An Abstract Real-Time Platform</a></li>
      </ul>
    </li>
    <li><a href="#further-system-design-learning-resources-2">Further System Design Learning Resources</a></li>
    <li><a href="#live-comment-system-design-deepdive">Live Comment System Design Deep Dive</a>
      <ul>
        <li><a href="#how-does-the-gateway-server-manage-client-connections">How Does the Gateway Server Manage Client Connections?</a></li>
        <li><a href="#how-to-handle-live-comments-on-multiple-livevideos">How to Handle Live Comments on Multiple Live Videos?</a></li>
        <li><a href="#how-to-support-massive-concurrent-clients-on-multiple-livevideos">How to Support Massive Concurrent Clients on Multiple Live Videos?</a></li>
        <li><a href="#scaling-live-comments-to-handle-peakload">Scaling Live Comments to Handle Peak Load</a></li>
        <li><a href="#what-is-the-subscribe-workflow-and-publish-workflow-for-live-comments">What Is the Subscribe Workflow and Publish Workflow for Live Comments?</a></li>
        <li><a href="#how-to-deploy-the-live-comment-service-across-multiple-datacenters">How to Deploy the Live Comment Service Across Multiple Data Centers?</a></li>
        <li><a href="#how-to-support-the-typing-indicators-on-livevideo">How to Support the Typing Indicators on Live Video?</a></li>
        <li><a href="#how-to-display-the-total-count-of-comments-on-each-livevideo">How to Display the Total Count of Comments on Each Live Video?</a></li>
        <li><a href="#scalability">Scalability</a></li>
        <li><a href="#latency">Latency</a></li>
        <li><a href="#concurrency">Concurrency</a></li>
        <li><a href="#high-availability">High Availability</a></li>
        <li><a href="#fault-tolerance">Fault Tolerance</a></li>
        <li><a href="#development-and-deployment">Development and Deployment</a></li>
        <li><a href="#durability">Durability</a></li>
        <li><a href="#operational-complexity">Operational Complexity</a></li>
      </ul>
    </li>
    <li><a href="#summary">Summary</a></li>
    <li><a href="#what-to-learn-next">What to learn next?</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#references">References</a></li>
  </ul>
</nav></div>
    </div><div class="content" id="content"><hr>
<p><em>The target audience for this article falls into the following roles:</em></p>
<ul>
<li><em>Tech workers</em></li>
<li><em>Students</em></li>
<li><em>Engineering managers</em></li>
</ul>
<p><em>The prerequisite to reading this article is fundamental knowledge of system design components. This article does not cover an in-depth guide on individual system design components.</em></p>
<p><em>Disclaimer: The system design questions are subjective. This article is written based on the research I have done on the topic and might differ from real-world implementations. Feel free to share your feedback and ask questions in the comments.</em></p>
<hr>
<hr>
<p><strong>Download my system design playbook for free on newsletter signup:</strong></p>
<div class="newsletter-container">
<iframe loading="lazy" class="newsletter-responsive-iframe" title="System Design Newsletter" src="https://newsletter.systemdesign.one/embed" scrolling="no"></iframe>
</div>
<hr>
<h2 id="how-does-the-live-commentwork">How Does the Live Comment Work?</h2>
<p>At a <strong>high level</strong>, the Live Comment service performs the following operations:</p>
<ul>
<li>gateway server fans out live comments to the clients through <a href="https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events" target="_blank" rel="noopener noreffer ">server-sent events</a> (<strong>SSE</strong>)</li>
<li>the client subscribes to a live video on the gateway server over Hypertext Transfer Protocol (<strong>HTTP</strong>)</li>
<li>in-memory subscription store on the gateway server keeps the viewership associativity</li>
<li>gateway server subscribes to a live video on the endpoint store</li>
<li>endpoint store keeps the set of gateway servers subscribed to a particular live video</li>
<li>heartbeat signal or time to live (<strong>TTL</strong>) on keys on the subscription store can be used to drain the inactive SSE connections</li>
<li>ending of a live video can trigger an update on the endpoint store</li>
<li>dispatcher broadcasts the live comments to the dispatcher in peer data centers</li>
</ul>
<hr>
<hr>
<hr>
<h2 id="what-is-a-livevideo">What is a Live Video?</h2>
<p>Live video is the streaming of video over the internet in real-time without prior recording and storage. Television broadcasts, video game streams, and social media videos are often live-streamed <sup id="fnref:1"><a href="#fn:1" class="footnote-ref" role="doc-noteref">1</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/likes-typing-indicator-live-comment.webp" title="Figure 1: Facebook Likes; Typing indicators; Live comments" data-thumbnail="/live-comment-system-design/likes-typing-indicator-live-comment.webp" data-sub-html="<h2>Figure 1: Facebook Likes; Typing indicators; Live comments</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/likes-typing-indicator-live-comment.webp"
            data-srcset="/live-comment-system-design/likes-typing-indicator-live-comment.webp, /live-comment-system-design/likes-typing-indicator-live-comment.webp 1.5x, /live-comment-system-design/likes-typing-indicator-live-comment.webp 2x"
            data-sizes="auto"
            alt="Figure 1: Facebook Likes; Typing indicators; Live comments" width="3281" height="1669" />
    </a><figcaption class="image-caption">Figure 1: Facebook Likes; Typing indicators; Live comments</figcaption>
    </figure>
<p>Live video is different from regular videos because live video results in spiky traffic patterns. Usually, Live videos are more engaging and tend to be watched thrice more than regular videos. On top of that, social networks such as Facebook show live videos at the top of the newsfeed, thereby increasing the probability of live videos being watched by the users (<strong>clients</strong>) <sup id="fnref:2"><a href="#fn:2" class="footnote-ref" role="doc-noteref">2</a></sup>.</p>
<hr>
<hr>
<hr>
<h2 id="what-are-live-comments">What are Live Comments?</h2>
<p>
<div style="position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden;">
  <iframe src="https://player.vimeo.com/video/812534164" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; border:0;" title="vimeo video" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
</div>

<div id="id-1">Live comments; Source: video by <a href="https://pixabay.com/users/josephredfield-8385382/" target="_blank" rel="noopener noreffer ">Joseph Redfield Nino</a> from <a href="https://pixabay.com/videos/livestreaming-instagram-facebook-26774/" target="_blank" rel="noopener noreffer ">Pixabay</a></div></p>
<p>Real-time experience makes the platform feel like a place of activity for the clients <sup id="fnref:3"><a href="#fn:3" class="footnote-ref" role="doc-noteref">3</a></sup>. Live commenting is a feature that allows clients to publish real-time comments on live videos. The live comments are usually a mixture of feedback from the clients on the live video or casual conversations between clients. Besides, live comments enable the streamer of the live video to engage with the clients during the live video broadcast.</p>
<hr>
<hr>
<hr>
<h2 id="terminology">Terminology</h2>
<p>The following terminology might be helpful for you:</p>
<ul>
<li>Node: a server that provides functionality to other services</li>
<li>Data replication: a technique of storing multiple copies of the same data on different nodes to improve the availability and durability of the system</li>
<li>Data partitioning: a technique of distributing data across multiple nodes to improve the performance and <a href="https://newsletter.systemdesign.one/p/micro-frontends" target="_blank" rel="noopener noreffer ">scalability</a> of the system</li>
<li>API: a software intermediary that allows two services to talk with each other</li>
<li>Fault tolerance: the ability of a service to recover from a failure without losing data</li>
<li>High availability: the ability of a service to remain reachable and not lose data even when a failure occurs</li>
</ul>
<hr>
<hr>
<hr>
<h2 id="questions-to-ask-the-interviewer">Questions to ask the Interviewer</h2>
<h3 id="candidate">Candidate</h3>
<ol>
<li>What are the primary use cases of the system?</li>
<li>Do live commenting service support only text?</li>
<li>Are the clients distributed across the globe?</li>
<li>What is the amount of Daily Active Users (<strong>DAU</strong>)?</li>
<li>What is the system&rsquo;s total number of daily live videos?</li>
<li>What is the average amount of live comments on a live video?</li>
<li>What is the anticipated read: write ratio of live comments?</li>
<li>What is the peak amount of concurrent users watching the same live video?</li>
<li>Should the comments on streamed videos be archived to save storage?</li>
</ol>
<hr>
<h3 id="interviewer">Interviewer</h3>
<ol>
<li>Clients can interact with each other in real-time over live comments on a Facebook live video</li>
<li>Yes</li>
<li>Yes</li>
<li>100 million DAU</li>
<li>200 million daily live videos</li>
<li>10</li>
<li>100: 1</li>
<li>80 million</li>
<li>Yes, there is no need to support the replay of live comments</li>
</ol>
<hr>
<hr>
<hr>
<h2 id="requirements">Requirements</h2>
<h3 id="functional-requirements">Functional Requirements</h3>
<ul>
<li>The <strong>publishers</strong> (client) can write real-time live comments on a Facebook live video</li>
<li>The <strong>receivers</strong> (clients) watching the Facebook live video should be able to view the live comments in real time</li>
<li>The receiver should be able to view an active real-time feed of live comments on each live video while scrolling across the Facebook newsfeed</li>
<li>The <strong>typing indicators</strong> (somebody is typing feature) should be supported</li>
<li>The total count of comments should be visible on each live video</li>
<li>The live comment service should handle clients across the globe</li>
<li>The live comment service should handle millions of concurrent clients</li>
</ul>
<hr>
<h3 id="non-functional-requirements">Non-Functional Requirements</h3>
<ul>
<li>Highly availability</li>
<li>Fault-tolerant</li>
<li>Low latency</li>
<li>Scalability</li>
<li>Eventual <a href="https://systemdesign.one/consistency-patterns/" target="_blank" rel="noopener noreffer ">consistency</a></li>
</ul>
<hr>
<hr>
<hr>
<h2 id="live-commenting-apidesign">Live Commenting API Design</h2>
<p>The real-time Application Programming Interface (<strong>API</strong>) can be implemented for faster user experiences and instant delivery of live comments. The average time for a human to blink is 100 ms, and the average reaction time for a human is around 250 ms. Therefore, the actions performed within 250 ms are perceived as real-time or live <sup id="fnref:4"><a href="#fn:4" class="footnote-ref" role="doc-noteref">4</a></sup>, <sup id="fnref:5"><a href="#fn:5" class="footnote-ref" role="doc-noteref">5</a></sup>. An event-driven architecture can be used to build a real-time data platform. The general subscription models for an API are the following <sup id="fnref:6"><a href="#fn:6" class="footnote-ref" role="doc-noteref">6</a></sup>:</p>
<ul>
<li>push-based (server-initiated)</li>
<li>pull-based (client-initiated)</li>
</ul>
<p>The popular protocols for an event-driven API are the following <sup id="fnref:6"><a href="#fn:6" class="footnote-ref" role="doc-noteref">6</a></sup>:</p>
<table>
<thead>
<tr>
<th>Protocol</th>
<th>Description</th>
<th>Use Cases</th>
<th>Subscription Model</th>
</tr>
</thead>
<tbody>
<tr>
<td>Webhook</td>
<td>HTTP-based callback function that allows lightweight, event-driven infrequent communication between APIs</td>
<td>trigger automation workflows</td>
<td>push-based</td>
</tr>
<tr>
<td>WebSub</td>
<td>communication channel for frequent messages between web content publishers and subscribers based on HTTP webhooks</td>
<td>news aggregator platforms, stock exchanges, and air traffic networks</td>
<td>push-based</td>
</tr>
<tr>
<td>WebSockets</td>
<td>provides full-duplex communication channels over a single TCP connection with lower overhead than half-duplex alternatives such as HTTP polling</td>
<td>financial tickers, location-based apps, and chat solutions</td>
<td>pull-based</td>
</tr>
<tr>
<td>SSE</td>
<td>lightweight and subscribe-only protocol for event-driven data streams</td>
<td>live score updates</td>
<td>pull-based</td>
</tr>
<tr>
<td>MQTT</td>
<td>protocol for streaming data between devices with limited CPU power and low bandwidth networks</td>
<td>Internet of Things</td>
<td>pull-based</td>
</tr>
</tbody>
</table>
<p>The communication protocol and subscription model chosen should be ideal to build a reliable and scalable live comment service. The pull-based subscription model is the optimal choice for the delivery of live comments to the receivers for the following reasons <sup id="fnref:6"><a href="#fn:6" class="footnote-ref" role="doc-noteref">6</a></sup>:</p>
<ul>
<li>the receiver only needs to view the live comments when the receiver is online (connected or subscribed to the live video)</li>
<li>the receiver can ignore the comments when disconnected or unsubscribed</li>
<li>non-trivial to predict the total count of clients that will be subscribed to watching a live video</li>
<li>the receivers will be geographically distributed across the globe</li>
<li>the connection from the receiver might be unpredictable</li>
</ul>
<figure><a class="lightgallery" href="/live-comment-system-design/rest-compared-with-sse.webp" title="Figure 2: REST compared with SSE" data-thumbnail="/live-comment-system-design/rest-compared-with-sse.webp" data-sub-html="<h2>Figure 2: REST compared with SSE</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/rest-compared-with-sse.webp"
            data-srcset="/live-comment-system-design/rest-compared-with-sse.webp, /live-comment-system-design/rest-compared-with-sse.webp 1.5x, /live-comment-system-design/rest-compared-with-sse.webp 2x"
            data-sizes="auto"
            alt="Figure 2: REST compared with SSE" width="2960" height="1716" />
    </a><figcaption class="image-caption">Figure 2: REST compared with SSE</figcaption>
    </figure>
<p>The client creates a regular HTTP long poll connection with the server with <a href="https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events" target="_blank" rel="noopener noreffer ">server-sent events</a> (<strong>SSE</strong>). The server can push a continuous stream of data to the client on the same connection as events occur. The client doesn&rsquo;t need to perform subsequent requests <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/http-long-poll-with-sse.webp" title="Figure 3: HTTP long poll with SSE" data-thumbnail="/live-comment-system-design/http-long-poll-with-sse.webp" data-sub-html="<h2>Figure 3: HTTP long poll with SSE</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/http-long-poll-with-sse.webp"
            data-srcset="/live-comment-system-design/http-long-poll-with-sse.webp, /live-comment-system-design/http-long-poll-with-sse.webp 1.5x, /live-comment-system-design/http-long-poll-with-sse.webp 2x"
            data-sizes="auto"
            alt="Figure 3: HTTP long poll with SSE" width="2074" height="1339" />
    </a><figcaption class="image-caption">Figure 3: HTTP long poll with SSE</figcaption>
    </figure>
<p>The only difference for SSE from a regular HTTP request is that the <em>Accept</em> header on the HTTP request holds the value <em>text/event-stream</em>. The <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventSource" target="_blank" rel="noopener noreffer ">EventSource interface</a> is used by the client to receive and process server-sent events independently in <em>text/event-stream</em> format without closing the connection. All modern web browsers support the EventSource interface natively. The EventSource interface can be implemented on <a href="https://github.com/inaka/EventSource" target="_blank" rel="noopener noreffer ">iOS</a> and <a href="https://github.com/tylerjroach/eventsource-android" target="_blank" rel="noopener noreffer ">Android</a> platforms with lightweight libraries <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>, <sup id="fnref:8"><a href="#fn:8" class="footnote-ref" role="doc-noteref">8</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/complexity-inversion-event-driven-api.webp" title="Figure 4: Complexity inversion in an event-driven API" data-thumbnail="/live-comment-system-design/complexity-inversion-event-driven-api.webp" data-sub-html="<h2>Figure 4: Complexity inversion in an event-driven API</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/complexity-inversion-event-driven-api.webp"
            data-srcset="/live-comment-system-design/complexity-inversion-event-driven-api.webp, /live-comment-system-design/complexity-inversion-event-driven-api.webp 1.5x, /live-comment-system-design/complexity-inversion-event-driven-api.webp 2x"
            data-sizes="auto"
            alt="Figure 4: Complexity inversion in an event-driven API" width="2184" height="1326" />
    </a><figcaption class="image-caption">Figure 4: Complexity inversion in an event-driven API</figcaption>
    </figure>
<p>The client is responsible for maintaining the state and execution of requests in the Representational state transfer (<strong>REST</strong>) paradigm. On the contrary, the server is responsible for maintaining the state and pushing updates to the client in an event-driven architecture. In layman&rsquo;s terms, the complexity of the receiver is kept minimum in an event-driven API as the receiver only has to create an SSE connection to view the live comments on a Facebook live video <sup id="fnref:6"><a href="#fn:6" class="footnote-ref" role="doc-noteref">6</a></sup>. In conclusion, SSE is the optimal protocol for the live comment service due to the following reasons <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>, <sup id="fnref:8"><a href="#fn:8" class="footnote-ref" role="doc-noteref">8</a></sup>:</p>
<ul>
<li>SSE works over traditional HTTP</li>
<li>SSE streams chunks of data over the same open HTTP connection</li>
</ul>
<p>The predefined fields of an SSE connection are the following <sup id="fnref:6"><a href="#fn:6" class="footnote-ref" role="doc-noteref">6</a></sup>:</p>
<table>
<thead>
<tr>
<th>Field</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>event</td>
<td>the event type defined by the server</td>
</tr>
<tr>
<td>data</td>
<td>the payload of the event</td>
</tr>
<tr>
<td>id</td>
<td>ID for each event</td>
</tr>
<tr>
<td>retry</td>
<td>the client attempts to reconnect with the server after a specific timeframe if the connection was closed</td>
</tr>
</tbody>
</table>
<p>The following are the drawbacks of SSE <sup id="fnref:6"><a href="#fn:6" class="footnote-ref" role="doc-noteref">6</a></sup>:</p>
<ul>
<li>the data format is restricted to transporting UTF-8 messages with no support for binary data</li>
<li>only up to six concurrent SSE connections can be opened per web browser on pre-HTTP/2 networks</li>
</ul>
<p>The components in the system expose the Application Programming Interface (API) endpoints to the client through Representational State Transfer (REST).
The description of HTTP <strong>Request headers</strong> is the following:</p>
<table>
<thead>
<tr>
<th>Header</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>accept</td>
<td>type of content the client can understand</td>
</tr>
<tr>
<td>authorization</td>
<td>authorize your user account</td>
</tr>
<tr>
<td>content-encoding</td>
<td>compression type used by the data payload</td>
</tr>
<tr>
<td>method</td>
<td>HTTP Verb</td>
</tr>
<tr>
<td>content-type</td>
<td>type of data format (JSON or XML)</td>
</tr>
</tbody>
</table>
<p>The description of HTTP <strong>Response headers</strong> is the following:</p>
<table>
<thead>
<tr>
<th>Header</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>status code</td>
<td>shows if the request was successful</td>
</tr>
<tr>
<td>content-type</td>
<td>type of data format</td>
</tr>
</tbody>
</table>
<hr>
<hr>
<h3 id="how-does-the-receiver-subscribe-to-a-specific-livevideo">How does the receiver subscribe to a specific live video?</h3>
<p>The client must subscribe to a live video for viewing the live comments. The client executes an HTTP PUT request for subscribing to a live video. The PUT requests are idempotent. The PUT method is used instead of the GET method because the in-memory subscription store will be modified when a client subscribes to a live video.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt"> 1
</span><span class="lnt"> 2
</span><span class="lnt"> 3
</span><span class="lnt"> 4
</span><span class="lnt"> 5
</span><span class="lnt"> 6
</span><span class="lnt"> 7
</span><span class="lnt"> 8
</span><span class="lnt"> 9
</span><span class="lnt">10
</span><span class="lnt">11
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="sr">/videos/</span><span class="o">:</span><span class="nx">video</span><span class="o">-</span><span class="nx">id</span><span class="o">/</span><span class="nx">subscriptions</span>
<span class="nx">method</span><span class="o">:</span> <span class="nx">PUT</span>
<span class="nx">accept</span><span class="o">:</span> <span class="nx">text</span><span class="o">/</span><span class="nx">event</span><span class="o">-</span><span class="nx">stream</span>
<span class="nx">authorization</span><span class="o">:</span> <span class="nx">Bearer</span> <span class="o">&lt;</span><span class="nx">JWT</span><span class="o">&gt;</span>
<span class="nx">content</span><span class="o">-</span><span class="nx">length</span><span class="o">:</span> <span class="mi">20</span>
<span class="nx">content</span><span class="o">-</span><span class="nx">type</span><span class="o">:</span> <span class="nx">application</span><span class="o">/</span><span class="nx">json</span>
<span class="nx">content</span><span class="o">-</span><span class="nx">encoding</span><span class="o">:</span> <span class="nx">gzip</span>

<span class="p">{</span>
<span class="nx">user_id</span><span class="o">:</span> <span class="o">&lt;</span><span class="kr">int</span><span class="o">&gt;</span>
<span class="p">}</span>
</code></pre></td></tr></table>
</div>
</div><p>The <em>accept: text/event-stream</em> HTTP request header indicates that the client is waiting for an open connection to the event stream from the server to fetch live comments <sup id="fnref:9"><a href="#fn:9" class="footnote-ref" role="doc-noteref">9</a></sup>. The server responds with status code <em>200 OK</em> on success.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span><span class="lnt">2
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="nx">status</span> <span class="nx">code</span><span class="o">:</span> <span class="mi">200</span> <span class="nx">OK</span>
<span class="nx">content</span><span class="o">-</span><span class="nx">type</span><span class="o">:</span> <span class="nx">text</span><span class="o">/</span><span class="nx">event</span><span class="o">-</span><span class="nx">stream</span>
</code></pre></td></tr></table>
</div>
</div><p>The <em>content-type: text/event-stream</em> HTTP response header indicates that the server established an open connection to the event stream to dispatch events to the client. The response event stream contains the live comments.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span><span class="lnt">2
</span><span class="lnt">3
</span><span class="lnt">4
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="nx">id</span><span class="o">:</span> <span class="mi">1</span>
<span class="nx">event</span><span class="o">:</span> <span class="nx">comment</span>
<span class="nx">data</span><span class="o">:</span> <span class="p">{</span><span class="s2">&#34;awesome&#34;</span><span class="p">}</span>
<span class="nx">data</span><span class="o">:</span> <span class="p">{</span><span class="s2">&#34;hey there&#34;</span><span class="p">}</span>
</code></pre></td></tr></table>
</div>
</div><hr>
<hr>
<h3 id="how-does-the-receiver-unsubscribe-from-a-livevideo">How does the receiver unsubscribe from a live video?</h3>
<p>The client should unsubscribe from a live video to stop receiving live comments. The client executes an HTTP DELETE request for unsubscribing from a live video <sup id="fnref:9"><a href="#fn:9" class="footnote-ref" role="doc-noteref">9</a></sup>. The DELETE requests are idempotent.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span><span class="lnt">2
</span><span class="lnt">3
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="sr">/videos/</span><span class="o">:</span><span class="nx">video</span><span class="o">-</span><span class="nx">id</span><span class="o">/</span><span class="nx">subscriptions</span><span class="o">/:</span><span class="nx">subscription</span><span class="o">-</span><span class="nx">id</span>
<span class="nx">method</span><span class="o">:</span> <span class="nx">DELETE</span>
<span class="nx">authorization</span><span class="o">:</span> <span class="nx">Bearer</span> <span class="o">&lt;</span><span class="nx">JWT</span><span class="o">&gt;</span>
</code></pre></td></tr></table>
</div>
</div><p>The server responds with status code <em>200 OK</em> on success.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="nx">status</span> <span class="nx">code</span><span class="o">:</span> <span class="mi">200</span> <span class="nx">OK</span>
</code></pre></td></tr></table>
</div>
</div><p>The server responds with status code <em>204 No Content</em> when the client successfully unsubscribes from a live video. The HTTP response body will be empty.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="nx">status</span> <span class="nx">code</span><span class="o">:</span> <span class="mi">204</span> <span class="nx">No</span> <span class="nx">Content</span>
</code></pre></td></tr></table>
</div>
</div><hr>
<hr>
<h3 id="how-does-the-client--publish-a-livecomment">How does the client  publish a live comment?</h3>
<p>The client executes an HTTP POST request to publish a live comment.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt"> 1
</span><span class="lnt"> 2
</span><span class="lnt"> 3
</span><span class="lnt"> 4
</span><span class="lnt"> 5
</span><span class="lnt"> 6
</span><span class="lnt"> 7
</span><span class="lnt"> 8
</span><span class="lnt"> 9
</span><span class="lnt">10
</span><span class="lnt">11
</span><span class="lnt">12
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="sr">/videos/</span><span class="o">:</span><span class="nx">video</span><span class="o">-</span><span class="nx">id</span><span class="o">/</span><span class="nx">comments</span>
<span class="nx">method</span><span class="o">:</span> <span class="nx">POST</span>
<span class="nx">accept</span><span class="o">:</span> <span class="nx">application</span><span class="o">/</span><span class="nx">json</span>
<span class="nx">authorization</span><span class="o">:</span> <span class="nx">Bearer</span> <span class="o">&lt;</span><span class="nx">JWT</span><span class="o">&gt;</span>
<span class="nx">content</span><span class="o">-</span><span class="nx">length</span><span class="o">:</span> <span class="mi">2000</span>
<span class="nx">content</span><span class="o">-</span><span class="nx">type</span><span class="o">:</span> <span class="nx">application</span><span class="o">/</span><span class="nx">json</span>
<span class="nx">content</span><span class="o">-</span><span class="nx">encoding</span><span class="o">:</span> <span class="nx">gzip</span>

<span class="p">{</span>
<span class="nx">user_id</span><span class="o">:</span> <span class="o">&lt;</span><span class="kr">int</span><span class="o">&gt;</span><span class="p">,</span>
<span class="nx">comment</span><span class="o">:</span> <span class="o">&lt;</span><span class="nx">object</span><span class="o">&gt;</span>
<span class="p">}</span>
</code></pre></td></tr></table>
</div>
</div><p>The server responds with status code <em>200 OK</em> on success.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="nx">status</span> <span class="nx">code</span><span class="o">:</span> <span class="mi">200</span> <span class="nx">OK</span>
</code></pre></td></tr></table>
</div>
</div><p>Alternatively, the server responds with status code <em>201 created</em> on success.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="nx">status</span> <span class="nx">code</span><span class="o">:</span> <span class="mi">201</span> <span class="nx">created</span>
</code></pre></td></tr></table>
</div>
</div><p>The server responds with status code <em>400 bad request</em> for indicating a failed request due to an invalid request payload from the client.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="nx">status</span> <span class="nx">code</span><span class="o">:</span> <span class="mi">400</span> <span class="nx">bad</span> <span class="nx">request</span>
</code></pre></td></tr></table>
</div>
</div><p>The client receives a status code <em>403 forbidden</em> when the client has valid credentials but insufficient privileges to act on the resource.</p>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-js" data-lang="js"><span class="nx">status</span> <span class="nx">code</span><span class="o">:</span> <span class="mi">403</span> <span class="nx">forbidden</span>
</code></pre></td></tr></table>
</div>
</div><hr>
<hr>
<hr>
<h2 id="further-system-design-learning-resources">Further System Design Learning Resources</h2>
<p><strong>Download my system design playbook for free on newsletter signup:</strong></p>
<div class="newsletter-container">
<iframe loading="lazy" class="newsletter-responsive-iframe" title="System Design Newsletter" src="https://newsletter.systemdesign.one/embed" scrolling="no"></iframe>
</div>
<hr>
<hr>
<hr>
<h2 id="live-comment-system-databasedesign">Live Comment System Database Design</h2>
<p>The live comment service is a read-heavy system. In simple words, the predominant usage pattern is the client viewing live comments.</p>
<h3 id="database-schemadesign">Database Schema Design</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/live-comment-database-schema.webp" title="Figure 5: Live comment database schema" data-thumbnail="/live-comment-system-design/live-comment-database-schema.webp" data-sub-html="<h2>Figure 5: Live comment database schema</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/live-comment-database-schema.webp"
            data-srcset="/live-comment-system-design/live-comment-database-schema.webp, /live-comment-system-design/live-comment-database-schema.webp 1.5x, /live-comment-system-design/live-comment-database-schema.webp 2x"
            data-sizes="auto"
            alt="Figure 5: Live comment database schema" width="2140" height="829" />
    </a><figcaption class="image-caption">Figure 5: Live comment database schema</figcaption>
    </figure>
<p>The major entities of the relational database are the comments table, the videos table, and the users table. The relationship between the users and the comments tables is <strong>1-to-many</strong>. The relationship between the videos and the comments tables is <strong>1-to-many</strong>. The relationship between the users and videos tables is <strong>1-to-many</strong>.</p>
<h4 id="comments-table">Comments table</h4>
<table>
<thead>
<tr>
<th>Column</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>id</td>
<td>ID to identify the comment</td>
</tr>
<tr>
<td>user_id (Foreign key)</td>
<td>publisher of the comment</td>
</tr>
<tr>
<td>video_id (Foreign key)</td>
<td>ID of the associated video</td>
</tr>
<tr>
<td>content</td>
<td>content of the comment</td>
</tr>
<tr>
<td>created_at</td>
<td>timestamp of creation</td>
</tr>
</tbody>
</table>
<h4 id="videos-table">Videos table</h4>
<table>
<thead>
<tr>
<th>Column</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>id</td>
<td>identifier of the video</td>
</tr>
<tr>
<td>title</td>
<td>title of the video</td>
</tr>
<tr>
<td>user_id (Foreign key)</td>
<td>streamer of the live video</td>
</tr>
<tr>
<td>created_at</td>
<td>timestamp of creation</td>
</tr>
</tbody>
</table>
<h4 id="users-table">Users table</h4>
<table>
<thead>
<tr>
<th>Column</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr>
<td>id</td>
<td>identifier of the user</td>
</tr>
<tr>
<td>name</td>
<td>name of the user</td>
</tr>
<tr>
<td>email</td>
<td>email of the user</td>
</tr>
<tr>
<td>profile_image</td>
<td>URL to fetch the profile image of user</td>
</tr>
<tr>
<td>last_login</td>
<td>last login timestamp of the user</td>
</tr>
<tr>
<td>created_at</td>
<td>timestamp of user account creation</td>
</tr>
</tbody>
</table>
<hr>
<hr>
<h3 id="sql">SQL</h3>
<p>Structured Query Language (<strong>SQL</strong>) is a domain-specific language for managing data stored in the relational database management system.</p>
<h4 id="write-a-sql-query-to-fetch-the-latest-ten-comments-on-the-live-video-with-35-as-the-video-id">Write a SQL query to fetch the latest ten comments on the live video with &ldquo;35&rdquo; as the video ID</h4>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span><span class="lnt">2
</span><span class="lnt">3
</span><span class="lnt">4
</span><span class="lnt">5
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-sql" data-lang="sql"><span class="k">SELECT</span><span class="w"> </span><span class="o">*</span><span class="w">
</span><span class="w"></span><span class="k">FROM</span><span class="w"> </span><span class="n">comments</span><span class="w">
</span><span class="w"></span><span class="k">WHERE</span><span class="w"> </span><span class="n">video_id</span><span class="o">=</span><span class="s2">&#34;35&#34;</span><span class="w">
</span><span class="w"></span><span class="k">ORDER</span><span class="w"> </span><span class="k">BY</span><span class="w"> </span><span class="n">created_at</span><span class="w"> </span><span class="k">DESC</span><span class="w">
</span><span class="w"></span><span class="k">LIMIT</span><span class="w"> </span><span class="mi">10</span><span class="p">;</span></code></pre></td></tr></table>
</div>
</div>
<h4 id="write-a-sql-query-to-insert-a-new-live-comment">Write a SQL query to insert a new live comment</h4>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span><span class="lnt">2
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-sql" data-lang="sql"><span class="k">INSERT</span><span class="w"> </span><span class="k">INTO</span><span class="w"> </span><span class="n">comments</span><span class="w"> </span><span class="p">(</span><span class="n">id</span><span class="p">,</span><span class="w"> </span><span class="n">user_id</span><span class="p">,</span><span class="w"> </span><span class="n">video_id</span><span class="p">,</span><span class="w"> </span><span class="n">content</span><span class="p">,</span><span class="w"> </span><span class="n">created_at</span><span class="p">)</span><span class="w">
</span><span class="w"></span><span class="k">VALUES</span><span class="w"> </span><span class="p">(</span><span class="mi">3</span><span class="p">,</span><span class="w"> </span><span class="s2">&#34;42&#34;</span><span class="p">,</span><span class="w"> </span><span class="s2">&#34;35&#34;</span><span class="p">,</span><span class="w"> </span><span class="s2">&#34;awesome&#34;</span><span class="p">,</span><span class="w"> </span><span class="s2">&#34;2050-08-22&#34;</span><span class="p">);</span></code></pre></td></tr></table>
</div>
</div>
<h4 id="write-a-sql-query-to-fetch-the-total-count-of-comments-on-each-video">Write a SQL query to fetch the total count of comments on each video</h4>
<div class="highlight"><div class="chroma">
<table class="lntable"><tr><td class="lntd">
<pre class="chroma"><code><span class="lnt">1
</span><span class="lnt">2
</span><span class="lnt">3
</span></code></pre></td>
<td class="lntd">
<pre class="chroma"><code class="language-sql" data-lang="sql"><span class="k">SELECT</span><span class="w"> </span><span class="k">COUNT</span><span class="p">(</span><span class="n">id</span><span class="p">),</span><span class="w"> </span><span class="n">video_id</span><span class="w">
</span><span class="w"></span><span class="k">FROM</span><span class="w"> </span><span class="n">comments</span><span class="w">
</span><span class="w"></span><span class="k">GROUP</span><span class="w"> </span><span class="k">BY</span><span class="w"> </span><span class="n">video_id</span><span class="p">;</span></code></pre></td></tr></table>
</div>
</div>
<hr>
<hr>
<h3 id="type-of-datastore">Type of Data Store</h3>
<p>The content of live comments contains only textual data and does not include any media files. A very fast and reliable database that not only persistently store data but also access the data quickly is a key feature for building the live comment service. The persistent storage of live comments is needed to retrieve the comments at a later point in time. In addition, the data kept in persistent storage can be used for auditing purposes <sup id="fnref:10"><a href="#fn:10" class="footnote-ref" role="doc-noteref">10</a></sup>. The relational database offers well-defined structures for comments, users, and videos. The relational database will be an optimal choice when the dataset is small. However, the relational database will be a suboptimal solution for live comment service due to the following scalability limitations <sup id="fnref:5"><a href="#fn:5" class="footnote-ref" role="doc-noteref">5</a></sup>:</p>
<ul>
<li>the internal data structure adds delay to the data operations</li>
<li>complex queries are needed to reintegrate data because of data segregation</li>
</ul>
<p>The creation of database indexes on <em>video_id</em> and <em>created_at</em> columns will improve the performance of the read operations at the expense of slow write operations. The NoSQL database such as <a href="https://cassandra.apache.org/_/index.html" target="_blank" rel="noopener noreffer ">Apache Cassandra</a> can be used as persistent data storage for live comments due to the following reasons <sup id="fnref:5"><a href="#fn:5" class="footnote-ref" role="doc-noteref">5</a></sup>:</p>
<ul>
<li>Log-structured merge-tree (<strong>LSM</strong>) based storage engine offers extremely high performance on writes</li>
<li>schemaless data model reduces the overhead of joining different tables</li>
<li>optimized natively for time series data</li>
</ul>
<p>Apache Cassandra is not optimized for read operations due to the nature of the LSM-based storage engine. An in-memory database such as Redis can be used in combination with Apache Cassandra to improve the read performance and make the data storage layer scalable and performant for live comments. The geo-replication enabled Redis <a href="https://newsletter.systemdesign.one/p/caching-patterns" target="_blank" rel="noopener noreffer ">cache</a> with a time to live (<strong>TTL</strong>) of 1 second can be added as a cache layer on top of Apache Cassandra to improve the read performance. In addition, live comments published on an extremely popular live video can be kept in the cache  on network edges to improve the latency <sup id="fnref:10"><a href="#fn:10" class="footnote-ref" role="doc-noteref">10</a></sup>.</p>
<p>The sets data type in Redis can be used to efficiently store the live comments. The native deduplication logic of sets data type ensures that live comments are stored in memory without having an additional logic to prevent repeated live comments. The <a href="https://redis.io/docs/data-types/sorted-sets/" target="_blank" rel="noopener noreffer ">sorted set</a> data type in Redis can be used for data integrity by maintaining the reverse chronological ordering of live comments. The sorted set data type can make use of the timestamp on live comments for sorting the live comments without implementing a custom sorting algorithm. The metadata of the publisher of a live comment can be stored in Redis hash data type for quick retrieval <sup id="fnref:4"><a href="#fn:4" class="footnote-ref" role="doc-noteref">4</a></sup>, <sup id="fnref:11"><a href="#fn:11" class="footnote-ref" role="doc-noteref">11</a></sup>, <sup id="fnref:5"><a href="#fn:5" class="footnote-ref" role="doc-noteref">5</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/live-comment-cap-theorem.webp" title="Figure 6: Live comments; AP in CAP Theorem" data-thumbnail="/live-comment-system-design/live-comment-cap-theorem.webp" data-sub-html="<h2>Figure 6: Live comments; AP in CAP Theorem</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/live-comment-cap-theorem.webp"
            data-srcset="/live-comment-system-design/live-comment-cap-theorem.webp, /live-comment-system-design/live-comment-cap-theorem.webp 1.5x, /live-comment-system-design/live-comment-cap-theorem.webp 2x"
            data-sizes="auto"
            alt="Figure 6: Live comments; AP in CAP Theorem" width="1548" height="722" />
    </a><figcaption class="image-caption">Figure 6: Live comments; AP in CAP Theorem</figcaption>
    </figure>
<p>The receivers who are geographically located closer to the publisher of the live comment will see the live comment instantly while the receivers who are located on a different continent might see the live comment with a slight delay (lower than 250 ms) to favor availability and partition tolerance in the CAP theorem <sup id="fnref:10"><a href="#fn:10" class="footnote-ref" role="doc-noteref">10</a></sup>.</p>
<hr>
<hr>
<hr>
<h2 id="capacity-planning">Capacity Planning</h2>
<p>The rate of clients viewing the live comments is significantly higher than the rate of clients publishing the live comments. The calculated numbers are approximations. A few helpful tips on <a href="https://systemdesign.one/back-of-the-envelope" target="_blank" rel="noopener noreffer ">capacity planning</a> during system design are the following:</p>
<ul>
<li>1 million requests/day = 12 requests/second</li>
<li>round off the numbers for quicker calculations</li>
<li>write down the units while doing conversions</li>
</ul>
<h3 id="traffic">Traffic</h3>
<p>The live comment service is a read-heavy system. The Daily Active Users (<strong>DAU</strong>) count is 100 million. On average, the total number of daily live videos is 200 million. A live video receives 10 comments on average.</p>
<table>
<thead>
<tr>
<th>Description</th>
<th>Value</th>
</tr>
</thead>
<tbody>
<tr>
<td>DAU (write)</td>
<td>2 billion</td>
</tr>
<tr>
<td>QPS (write)</td>
<td>12 thousand</td>
</tr>
<tr>
<td>read: write</td>
<td>100: 1</td>
</tr>
<tr>
<td>QPS (read)</td>
<td>1.2 million</td>
</tr>
</tbody>
</table>
<hr>
<hr>
<h3 id="storage">Storage</h3>
<p>The comments on a streamed live video can be archived and kept in a cold store to save storage costs. In addition, comments older than a specific time frame can be removed to minimize storage costs. The storage size of each character on a live comment is assumed to be 1 byte.</p>
<table>
<thead>
<tr>
<th>Description</th>
<th>Size (bytes)</th>
</tr>
</thead>
<tbody>
<tr>
<td>id</td>
<td>20</td>
</tr>
<tr>
<td>user_id</td>
<td>20</td>
</tr>
<tr>
<td>video_id</td>
<td>20</td>
</tr>
<tr>
<td>content</td>
<td>2000</td>
</tr>
<tr>
<td>created_at</td>
<td>10</td>
</tr>
</tbody>
</table>
<table>
<thead>
<tr>
<th>Description</th>
<th>Calculation</th>
<th>Total</th>
</tr>
</thead>
<tbody>
<tr>
<td>storage for a day</td>
<td>12k comments/sec * 2 KB/comment * 86400 sec/day</td>
<td>2 TB</td>
</tr>
</tbody>
</table>
<p>In total, a live comment is approximately 2 KB in size. The replication factor for storage can be set to three for improved durability and disaster recovery.</p>
<hr>
<hr>
<h3 id="bandwidth">Bandwidth</h3>
<p><strong>Ingress</strong> is the network traffic that enters the server when live comments are written. <strong>Egress</strong> is the network traffic that exits the servers when live comments are viewed. The network bandwidth is spread out across the globe depending on the location of the clients.</p>
<table>
<thead>
<tr>
<th>Description</th>
<th>Calculation</th>
<th>Total</th>
</tr>
</thead>
<tbody>
<tr>
<td>Ingress</td>
<td>2 billion comments/day * 2 KB/comment * 10^(-5) day/sec</td>
<td>40 MB/sec</td>
</tr>
<tr>
<td>Egress</td>
<td>200 billion comments/day * 2 KB/comment * 10^(-5) day/sec</td>
<td>4 GB/sec</td>
</tr>
</tbody>
</table>
<hr>
<hr>
<h3 id="memory">Memory</h3>
<p>The in-memory subscription store keeps the client viewership associations.</p>
<table>
<thead>
<tr>
<th>Description</th>
<th>Calculation</th>
<th>Total</th>
</tr>
</thead>
<tbody>
<tr>
<td>Subscription store</td>
<td>100 million users/day * 16 bytes/user</td>
<td>1.6 GB/day</td>
</tr>
</tbody>
</table>
<hr>
<hr>
<hr>
<h2 id="further-system-design-learning-resources-1">Further System Design Learning Resources</h2>
<p><strong>Download my system design playbook for free on newsletter signup:</strong></p>
<div class="newsletter-container">
<iframe loading="lazy" class="newsletter-responsive-iframe" title="System Design Newsletter" src="https://newsletter.systemdesign.one/embed" scrolling="no"></iframe>
</div>
<hr>
<hr>
<hr>
<h2 id="live-commenting-high-level-design">Live Commenting High-Level Design</h2>
<h3 id="write-globally-and-readlocally">Write Globally and Read Locally</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/write-globally-read-locally.webp" title="Figure 7: Write globally and read locally through asynchronous database replication" data-thumbnail="/live-comment-system-design/write-globally-read-locally.webp" data-sub-html="<h2>Figure 7: Write globally and read locally through asynchronous database replication</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/write-globally-read-locally.webp"
            data-srcset="/live-comment-system-design/write-globally-read-locally.webp, /live-comment-system-design/write-globally-read-locally.webp 1.5x, /live-comment-system-design/write-globally-read-locally.webp 2x"
            data-sizes="auto"
            alt="Figure 7: Write globally and read locally through asynchronous database replication" width="2232" height="1180" />
    </a><figcaption class="image-caption">Figure 7: Write globally and read locally through asynchronous database replication</figcaption>
    </figure>
<p>The database can be configured to asynchronously replicate the data across the database servers in data centers located on distinct continents. In layman&rsquo;s terms, the data is always fetched by the server from the database servers in the local data center while the data updates are asynchronously written to database servers in data centers across the globe. This technique is known as <strong>writing globally and reading locally</strong>. For instance, when a client located in the US publishes a live comment on a Facebook live video, the clients located in Europe will not see the live comment instantaneously due to the asynchronous nature of data replication. The following are the drawbacks of writing globally and reading locally approach for implementing live comment service <sup id="fnref:12"><a href="#fn:12" class="footnote-ref" role="doc-noteref">12</a></sup>:</p>
<ul>
<li>significant bandwidth usage on data replication</li>
<li>live comments will not be real-time due to asynchronous replication</li>
<li>poor overall latency</li>
</ul>
<p>In summary, do not use the writing globally and reading locally approach for implementing live comments.</p>
<hr>
<hr>
<h3 id="write-locally-and-readglobally">Write Locally and Read Globally</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/write-locally-read-globally-pull-based.webp" title="Figure 8: Write locally and read globally through timestamp query (pull-based model)" data-thumbnail="/live-comment-system-design/write-locally-read-globally-pull-based.webp" data-sub-html="<h2>Figure 8: Write locally and read globally through timestamp query (pull-based model)</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/write-locally-read-globally-pull-based.webp"
            data-srcset="/live-comment-system-design/write-locally-read-globally-pull-based.webp, /live-comment-system-design/write-locally-read-globally-pull-based.webp 1.5x, /live-comment-system-design/write-locally-read-globally-pull-based.webp 2x"
            data-sizes="auto"
            alt="Figure 8: Write locally and read globally through timestamp query (pull-based model)" width="2232" height="1180" />
    </a><figcaption class="image-caption">Figure 8: Write locally and read globally through timestamp query (pull-based model)</figcaption>
    </figure>
<p>The data is always written to the database server in the local data center while the data is fetched by querying the servers in data centers across the globe as shown in Figure 8. This technique is known as the <strong>pull-based model of writing locally and reading globally</strong>. The timestamp on the live comments can be used to check if there are newer comments published since the last query execution. The server in the local data center will consolidate all the published live comments and return the response to the client. The bandwidth usage is relatively lower because the data is not replicated globally. However, the pull-based model will result in degraded latency because the server must query all the data centers across the globe on each read operation <sup id="fnref:12"><a href="#fn:12" class="footnote-ref" role="doc-noteref">12</a></sup>. In summary, do not use the pull-based model of writing locally and reading globally approach for implementing live comments.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/write-locally-read-globally-push-based.webp" title="Figure 9: Write locally and read globally through broadcasting (push-based model)" data-thumbnail="/live-comment-system-design/write-locally-read-globally-push-based.webp" data-sub-html="<h2>Figure 9: Write locally and read globally through broadcasting (push-based model)</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/write-locally-read-globally-push-based.webp"
            data-srcset="/live-comment-system-design/write-locally-read-globally-push-based.webp, /live-comment-system-design/write-locally-read-globally-push-based.webp 1.5x, /live-comment-system-design/write-locally-read-globally-push-based.webp 2x"
            data-sizes="auto"
            alt="Figure 9: Write locally and read globally through broadcasting (push-based model)" width="2232" height="1180" />
    </a><figcaption class="image-caption">Figure 9: Write locally and read globally through broadcasting (push-based model)</figcaption>
    </figure>
<p>The data is always written to the distributed database server in the local data center. In Figure 9, when a client publishes a live comment on a Facebook live video, the write operation is broadcast by the server to multiple data centers across the globe. This technique is known as the <strong>push-based model of writing locally and reading globally</strong>. The push-based model of writing locally and reading globally significantly reduces the usage of expensive bandwidth and improves the latency resulting in real-time live comments <sup id="fnref:12"><a href="#fn:12" class="footnote-ref" role="doc-noteref">12</a></sup>. In summary, use the push-based model of writing locally and reading globally for implementing live comments.</p>
<hr>
<hr>
<h3 id="prototyping-a-live-commentservice">Prototyping a Live Comment Service</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/prototyping-live-comment.webp" title="Figure 10: Prototyping a Live comment service" data-thumbnail="/live-comment-system-design/prototyping-live-comment.webp" data-sub-html="<h2>Figure 10: Prototyping a Live comment service</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/prototyping-live-comment.webp"
            data-srcset="/live-comment-system-design/prototyping-live-comment.webp, /live-comment-system-design/prototyping-live-comment.webp 1.5x, /live-comment-system-design/prototyping-live-comment.webp 2x"
            data-sizes="auto"
            alt="Figure 10: Prototyping a Live comment service" width="2143" height="1240" />
    </a><figcaption class="image-caption">Figure 10: Prototyping a Live comment service</figcaption>
    </figure>
<p>The clients watching a live video are known as <strong>receivers</strong> and the clients publishing live comments on a live video are known as <strong>publishers</strong>. The server holding the SSE connections to the receivers is known as the <strong>gateway server</strong>. A dedicated disk-based <strong>subscription store</strong> can be provisioned to persist the lifecycle of the client connections. The <strong>dispatcher</strong> is an abstraction layer for publishing data objects such as live comments or <a href="https://systemdesign.one/distributed-counter-system-design/" target="_blank" rel="noopener noreffer ">Facebook likes</a> by the client. The following operations are performed when a live comment is published on a live video <sup id="fnref:13"><a href="#fn:13" class="footnote-ref" role="doc-noteref">13</a></sup>:</p>
<ol>
<li>receiver subscribes to a live video on the gateway server</li>
<li>gateway server persists the metadata of the client connection in the subscription store</li>
<li>the publisher publishes a live comment on a live video</li>
<li>dispatcher queries the subscription store for all viewership associations on the live video</li>
<li>dispatcher publishes the live comment to the gateway server</li>
<li>gateway server delivers the live comment to all subscribed receivers</li>
</ol>
<p>The gateway server maintains the SSE connections with the clients. The subscription store should be replicated at least thrice for reliability and durability <sup id="fnref:13"><a href="#fn:13" class="footnote-ref" role="doc-noteref">13</a></sup>. The subscription store will become a bottleneck to scaling the live comment service because every read and write operation on live comments must query the subscription store. On top of that, the client connections to a specific live video are short-lived because clients scroll through the Facebook newsfeed. As a result, the subscription store must be updated frequently. In summary, the current prototype will not meet the requirements of a scalable and reliable live comment service.</p>
<hr>
<hr>
<h3 id="distribution-of-livecomments">Distribution of Live Comments</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/distribution-live-comment.webp" title="Figure 11: Distribution of live comments through SSE" data-thumbnail="/live-comment-system-design/distribution-live-comment.webp" data-sub-html="<h2>Figure 11: Distribution of live comments through SSE</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/distribution-live-comment.webp"
            data-srcset="/live-comment-system-design/distribution-live-comment.webp, /live-comment-system-design/distribution-live-comment.webp 1.5x, /live-comment-system-design/distribution-live-comment.webp 2x"
            data-sizes="auto"
            alt="Figure 11: Distribution of live comments through SSE" width="1756" height="942" />
    </a><figcaption class="image-caption">Figure 11: Distribution of live comments through SSE</figcaption>
    </figure>
<p>The receiver can periodically request (pull-based) the server to check whether new live comments were published. The periodic polling of the server will result in empty responses consuming unnecessary bandwidth. Moreover, the polling interval for live comments should be lower than 250 ms for a real-time experience. The increased polling frequency will highly likely overload the servers. The push-based approach using SSE persistent connections is optimal for the real-time delivery of live comments to the receivers <sup id="fnref:12"><a href="#fn:12" class="footnote-ref" role="doc-noteref">12</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/subscription-store-gateway.webp" title="Figure 12: In-memory subscription store on gateway server keeping viewership associations" data-thumbnail="/live-comment-system-design/subscription-store-gateway.webp" data-sub-html="<h2>Figure 12: In-memory subscription store on gateway server keeping viewership associations</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/subscription-store-gateway.webp"
            data-srcset="/live-comment-system-design/subscription-store-gateway.webp, /live-comment-system-design/subscription-store-gateway.webp 1.5x, /live-comment-system-design/subscription-store-gateway.webp 2x"
            data-sizes="auto"
            alt="Figure 12: In-memory subscription store on gateway server keeping viewership associations" width="2073" height="1243" />
    </a><figcaption class="image-caption">Figure 12: In-memory subscription store on gateway server keeping viewership associations</figcaption>
    </figure>
<p>The SSE connections to the client are non-blocking fire-and-forget for improved performance <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>. An in-memory subscription store using Redis can be provisioned locally on the gateway server to store the one-to-one associations between the receivers and the Facebook live video. The <a href="https://redis.io/docs/data-types/sets/" target="_blank" rel="noopener noreffer ">sets</a> data type in Redis can be used to efficiently store the ephemeral viewership associations. The subscription store is queried to identify the receivers who should receive the live comments on a specific live video <sup id="fnref:12"><a href="#fn:12" class="footnote-ref" role="doc-noteref">12</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/live-comment-delivery-real-time-platform.webp" title="Figure 13: Live comment delivery through the real-time platform" data-thumbnail="/live-comment-system-design/live-comment-delivery-real-time-platform.webp" data-sub-html="<h2>Figure 13: Live comment delivery through the real-time platform</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/live-comment-delivery-real-time-platform.webp"
            data-srcset="/live-comment-system-design/live-comment-delivery-real-time-platform.webp, /live-comment-system-design/live-comment-delivery-real-time-platform.webp 1.5x, /live-comment-system-design/live-comment-delivery-real-time-platform.webp 2x"
            data-sizes="auto"
            alt="Figure 13: Live comment delivery through the real-time platform" width="1765" height="776" />
    </a><figcaption class="image-caption">Figure 13: Live comment delivery through the real-time platform</figcaption>
    </figure>
<p>An abstract and scalable real-time platform can be built for real-time experiences such as displaying <a href="https://systemdesign.one/real-time-presence-platform-system-design/" target="_blank" rel="noopener noreffer ">real-time presence</a>, push notifications, <a href="https://systemdesign.one/distributed-counter-system-design/" target="_blank" rel="noopener noreffer ">Facebook likes</a>, <a href="https://systemdesign.one/distributed-counter-system-design/" target="_blank" rel="noopener noreffer ">Facebook reactions</a>, typing indicators, and live comments. The real-time platform can be reused across multiple platforms such as Facebook, Messenger, Instagram, Twitch, TikTok, LinkedIn, or YouTube <sup id="fnref:13"><a href="#fn:13" class="footnote-ref" role="doc-noteref">13</a></sup>. A dynamically configurable plugin system architecture should be implemented to create an abstract real-time platform. Plugins are embedded executable code for additional logic that gets invoked on distinct events such as the publishing of a live comment <sup id="fnref:3"><a href="#fn:3" class="footnote-ref" role="doc-noteref">3</a></sup>. The principle behind building a scalable distributed system is to start small and iteratively add simple layers to the architecture <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>. The general guidelines for horizontally scaling a service are the following <sup id="fnref:14"><a href="#fn:14" class="footnote-ref" role="doc-noteref">14</a></sup>:</p>
<ul>
<li>keep the service stateless</li>
<li>partition the service</li>
<li>replicate the service</li>
</ul>
<hr>
<hr>
<h3 id="live-commenting-with-pub-subserver">Live Commenting With Pub-Sub Server</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/live-comment-pub-sub.webp" title="Figure 14: Pub-sub server for live comments" data-thumbnail="/live-comment-system-design/live-comment-pub-sub.webp" data-sub-html="<h2>Figure 14: Pub-sub server for live comments</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/live-comment-pub-sub.webp"
            data-srcset="/live-comment-system-design/live-comment-pub-sub.webp, /live-comment-system-design/live-comment-pub-sub.webp 1.5x, /live-comment-system-design/live-comment-pub-sub.webp 2x"
            data-sizes="auto"
            alt="Figure 14: Pub-sub server for live comments" width="1516" height="785" />
    </a><figcaption class="image-caption">Figure 14: Pub-sub server for live comments</figcaption>
    </figure>
<p><a href="https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern" target="_blank" rel="noopener noreffer ">Publish-Subscribe</a> (<strong>pub-sub</strong>) pattern allows services running on distinct technologies to communicate with each other <sup id="fnref:11"><a href="#fn:11" class="footnote-ref" role="doc-noteref">11</a></sup>. The pub-sub technique using the message bus enables a producer to send live comments (<strong>messages</strong>) to multiple consumers. The services can communicate with each other instantly without having preset intervals for polling data from relevant data sources in a reactive architecture <sup id="fnref:5"><a href="#fn:5" class="footnote-ref" role="doc-noteref">5</a></sup>.</p>
<hr>
<h4 id="using-apache-kafka-as-the-pub-sub-server">Using Apache Kafka as the Pub-Sub Server</h4>
<figure><a class="lightgallery" href="/live-comment-system-design/kafka-pub-sub.webp" title="Figure 15: Live comments; Apache Kafka as the pub-sub server" data-thumbnail="/live-comment-system-design/kafka-pub-sub.webp" data-sub-html="<h2>Figure 15: Live comments; Apache Kafka as the pub-sub server</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/kafka-pub-sub.webp"
            data-srcset="/live-comment-system-design/kafka-pub-sub.webp, /live-comment-system-design/kafka-pub-sub.webp 1.5x, /live-comment-system-design/kafka-pub-sub.webp 2x"
            data-sizes="auto"
            alt="Figure 15: Live comments; Apache Kafka as the pub-sub server" width="1986" height="910" />
    </a><figcaption class="image-caption">Figure 15: Live comments; Apache Kafka as the pub-sub server</figcaption>
    </figure>
<p>Apache Kafka is configured as the pub-sub server to decouple the producers and consumers. The live comment service can be reliably scaled through the separation of concerns. The streaming protocol of <a href="https://kafka.apache.org/protocol.html" target="_blank" rel="noopener noreffer ">Kafka</a> offers lower overheads per message and provides ordering guarantees, integrity guarantees, and idempotency. Besides, the Kafka streaming protocol enables sharding the data before <a href="https://newsletter.systemdesign.one/p/what-is-critical-rendering-path" target="_blank" rel="noopener noreffer ">streaming</a> the data to consumers <sup id="fnref:6"><a href="#fn:6" class="footnote-ref" role="doc-noteref">6</a></sup>, <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>. The system design of live comments is challenging for the following reasons:</p>
<ul>
<li>clients will continuously scroll through the Facebook <a href="https://newsletter.systemdesign.one/p/feed-architecture" target="_blank" rel="noopener noreffer ">newsfeed</a></li>
<li>live videos visible on the viewport of the client will change frequently</li>
</ul>
<p>There will be multiple receivers (<strong>consumers</strong>) watching a live video. The live comments published on the live video should be consumed by all the receivers. When the client scrolls through the Facebook newsfeed or navigates outside the comments panel, the client is not supposed to see the live comments anymore. The consumer should dynamically unsubscribe from the  specific live video (<strong>Kafka topic</strong>) to stop seeing the live comments on the specific live video. However, unsubscribing from a Kafka topic is an expensive operation <sup id="fnref:15"><a href="#fn:15" class="footnote-ref" role="doc-noteref">15</a></sup>, <sup id="fnref:16"><a href="#fn:16" class="footnote-ref" role="doc-noteref">16</a></sup>. In addition, tracking the gateway servers consuming specific live videos is non-trivial resulting in oversubscription to all Kafka topics. The limitations of using Apache Kafka for building the live comment service can be summarized as the following <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ul>
<li>degraded latency because consumers use a pull-based model</li>
<li>limited scalability because each consumer should subscribe to all the Kafka topics and consume all the messages</li>
<li>operational complexity of Apache Kafka is relatively high</li>
</ul>
<p>The Kafka topics can be partitioned by the stream IDs at the expense of unbalanced partitions. Consumers can subscribe to specific partitions using <a href="https://systemdesign.one/consistent-hashing-explained/" target="_blank" rel="noopener noreffer ">consistent hashing</a> <sup id="fnref:3"><a href="#fn:3" class="footnote-ref" role="doc-noteref">3</a></sup>. Nonetheless, the consumers will not know in advance the stream IDs that the clients will be interested in. In a typical scenario, a server will have a diverse set of clients interested in almost all the live videos resulting in subscriptions to all the live videos. <a href="https://pulsar.apache.org/" target="_blank" rel="noopener noreffer ">Apache Pulsar</a> also runs into a similar set of challenges as Apache Kafka for the use case of live comment service <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>. In summary, do not use Apache Kafka as the pub-sub server for implementing live comments.</p>
<hr>
<hr>
<h4 id="using-redis-as-the-pub-sub-server">Using Redis as the Pub-Sub Server</h4>
<figure><a class="lightgallery" href="/live-comment-system-design/redis-cluster-pub-sub.webp" title="Figure 16: Live comments; Redis cluster as the pub-sub server" data-thumbnail="/live-comment-system-design/redis-cluster-pub-sub.webp" data-sub-html="<h2>Figure 16: Live comments; Redis cluster as the pub-sub server</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/redis-cluster-pub-sub.webp"
            data-srcset="/live-comment-system-design/redis-cluster-pub-sub.webp, /live-comment-system-design/redis-cluster-pub-sub.webp 1.5x, /live-comment-system-design/redis-cluster-pub-sub.webp 2x"
            data-sizes="auto"
            alt="Figure 16: Live comments; Redis cluster as the pub-sub server" width="1748" height="1162" />
    </a><figcaption class="image-caption">Figure 16: Live comments; Redis cluster as the pub-sub server</figcaption>
    </figure>
<p>The clients must use a protocol named REdis Serialization Protocol (<strong>RESP</strong>) for communicating with the Redis server. Redis can be used as the <a href="https://redis.io/docs/manual/pubsub/" target="_blank" rel="noopener noreffer ">pub-sub</a> server to transmit live comments (<strong>messages</strong>) between nodes <sup id="fnref:4"><a href="#fn:4" class="footnote-ref" role="doc-noteref">4</a></sup>. The Redis cluster can be used to <a href="https://newsletter.systemdesign.one/p/scalable-software-architecture" target="_blank" rel="noopener noreffer ">scale</a> the pub-sub server. The Redis pub-sub server replicates every incoming message to all nodes because Redis does not know the set of clients (receivers) on a specific node resulting in degraded performance. The <a href="https://systemdesign.one/consistent-hashing-explained/" target="_blank" rel="noopener noreffer ">consistent hashing</a> algorithm is used for load-balancing client subscriptions to live videos between a set of independent Redis nodes for improved scalability. <a href="https://systemdesign.one/consistent-hashing-explained/" target="_blank" rel="noopener noreffer ">Consistent hashing</a> significantly reduces the movement of live connections between nodes on a node failure or node addition. In layman&rsquo;s terms, the incoming messages are published to all Redis nodes but the server only listens on a small set of nodes for a specific subscription (live video) for improved scalability. The Redis replicas should be provisioned on every node to perform automatic failover when a node failure occurs <sup id="fnref:3"><a href="#fn:3" class="footnote-ref" role="doc-noteref">3</a></sup>.</p>
<p>A TCP connection is maintained between the producer to Redis and Redis to the consumer for delivering messages. As an unconventional workaround, the server could terminate the TCP connection between the consumer node and the pub-sub server for unsubscribing the consumer from a topic (live video) when the client scrolls through the Facebook newsfeed. The drawbacks of using the Redis pub-sub server for live comment service are the following:</p>
<ul>
<li>no guaranteed  at least one-time message delivery</li>
<li>reliability of message delivery depends on the TCP connection</li>
<li>messages might get lost due to a lack of message persistence</li>
</ul>
<p>In conclusion, do not use the Redis pub-sub server solution for implementing live comments.</p>
<hr>
<hr>
<h4 id="using-redis-streams-as-the-pub-sub-server">Using Redis Streams as the Pub-Sub Server</h4>
<hr>
<p>The Redis stream is a data structure that can be used like an append-only log for improving the responsiveness of the system. In simple words, Redis streams are functionally very equivalent to Apache Kafka. The Redis streams as the pub-sub server offer the following benefits <sup id="fnref:17"><a href="#fn:17" class="footnote-ref" role="doc-noteref">17</a></sup>, <sup id="fnref:18"><a href="#fn:18" class="footnote-ref" role="doc-noteref">18</a></sup>, <sup id="fnref:5"><a href="#fn:5" class="footnote-ref" role="doc-noteref">5</a></sup>, <sup id="fnref:19"><a href="#fn:19" class="footnote-ref" role="doc-noteref">19</a></sup>, <sup id="fnref:20"><a href="#fn:20" class="footnote-ref" role="doc-noteref">20</a></sup>, <sup id="fnref:21"><a href="#fn:21" class="footnote-ref" role="doc-noteref">21</a></sup>:</p>
<ul>
<li>guaranteed at least one-time delivery of messages</li>
<li>reliable messages through persistence with Append Only File (<strong>AOF</strong>) or Redis Database (<strong>RDB</strong>) persistency methods</li>
<li>more tolerant to network partitions</li>
<li>improved latency due to efficient in-memory storage</li>
<li>relatively trivial to provision and operate</li>
<li>messages can be consumed either in blocking or non-blocking methods</li>
</ul>
<p>The Redis Sentinel or Redis Active-Active configuration provides high availability for the cluster at the expense of increased operational complexity. The Redis Sentinel is a distributed system that monitors and provides failover policies to Redis instances. Redis streams could dynamically unsubscribe consumers  from a specific topic (live video). However, there is a risk of data loss in Redis streams because the data is only periodically written to disk. The memory limitation could also be a bottleneck to building a scalable live commenting service <sup id="fnref:19"><a href="#fn:19" class="footnote-ref" role="doc-noteref">19</a></sup>, <sup id="fnref:21"><a href="#fn:21" class="footnote-ref" role="doc-noteref">21</a></sup>. In conclusion, do not use Redis streams as the pub-sub server solution for implementing live comments.</p>
<hr>
<hr>
<h3 id="an-abstract-real-time-platform">An Abstract Real-Time Platform</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/live-comment-high-level-design.webp" title="Figure 17: Live comment; High-level design" data-thumbnail="/live-comment-system-design/live-comment-high-level-design.webp" data-sub-html="<h2>Figure 17: Live comment; High-level design</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/live-comment-high-level-design.webp"
            data-srcset="/live-comment-system-design/live-comment-high-level-design.webp, /live-comment-system-design/live-comment-high-level-design.webp 1.5x, /live-comment-system-design/live-comment-high-level-design.webp 2x"
            data-sizes="auto"
            alt="Figure 17: Live comment; High-level design" width="2687" height="1772" />
    </a><figcaption class="image-caption">Figure 17: Live comment; High-level design</figcaption>
    </figure>
<p>An in-memory <strong>subscription store</strong> can be provisioned using Redis on the same machine where the gateway server runs. The in-memory subscription store will hold the mapping between live videos and receivers (client connections). The gateway server can perform throttling and include additional product logic. A dedicated disk-based <strong>endpoint store</strong> can be used to identify the gateway servers interested in a specific live video. The endpoint store will contain a mapping between the live videos and a set of gateway servers. The following operations are performed when a live comment is published on a live video <sup id="fnref:13"><a href="#fn:13" class="footnote-ref" role="doc-noteref">13</a></sup>:</p>
<ol>
<li>receiver subscribes to a live video on the gateway server</li>
<li>the gateway server updates the subscription store with viewership association and informs the endpoint store that the gateway server is interested in the specific live video</li>
<li>the publisher writes a live comment on the specific live video</li>
<li>dispatcher queries one of the replicas of the endpoint store to fetch the set of subscribed gateway servers</li>
<li>the dispatcher forwards the live comment to the subscribed gateway servers</li>
<li>the gateway server checks the local in-memory subscription store and fans out the live comment to subscribed receivers</li>
</ol>
<p>The following operations can be executed for removing the expired records from the in-memory subscription store on the gateway server:</p>
<ul>
<li>the gateway server can send periodic heartbeat signals to the receivers to check if they are still connected</li>
<li>Redis TTL keys can expire the subscription record when the heartbeat signals show a failure</li>
<li>client-side JavaScript logic can trigger an event when the client scrolls away from the live video</li>
</ul>
<p>The endpoint store should remove the live video only when the set of gateway servers subscribed to the live video is empty. Redis set data type allows checking the cardinality of the set in constant time complexity. The ending of a live video can trigger the expiry of the records in the disk-based endpoint store. In summary, the current architecture can satisfy the requirements of a scalable and reliable live comment service.</p>
<hr>
<hr>
<hr>
<h2 id="further-system-design-learning-resources-2">Further System Design Learning Resources</h2>
<p><strong>Download my system design playbook for free on newsletter signup:</strong></p>
<div class="newsletter-container">
<iframe loading="lazy" class="newsletter-responsive-iframe" title="System Design Newsletter" src="https://newsletter.systemdesign.one/embed" scrolling="no"></iframe>
</div>
<hr>
<hr>
<hr>
<h2 id="live-comment-system-design-deepdive">Live Comment System Design Deep Dive</h2>
<p>Live experiences are underpinned by real-time, event-driven APIs for meeting the requirements of clients. A real-time platform is predicated upon low latency, data integrity, fault tolerance, availability, and scalability <sup id="fnref:4"><a href="#fn:4" class="footnote-ref" role="doc-noteref">4</a></sup>.</p>
<h3 id="how-does-the-gateway-server-manage-client-connections">How Does the Gateway Server Manage Client Connections?</h3>
<p>An <a href="https://en.wikipedia.org/wiki/Actor_model" target="_blank" rel="noopener noreffer ">actor</a> is an extremely lightweight object that can receive messages and take actions to handle the messages. The actor is decoupled from the source of the message. The actor is only responsible for recognizing the type of message received and performing the required action. A thread will be assigned to an actor when a message must be processed. The thread is released once the message is processed and the thread is assigned to the next actor. The total count of threads will be proportional to the count of CPU cores. A relatively small count of threads can handle a significant number of concurrent actors because a thread is assigned to an actor only during the execution time <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>, <sup id="fnref:8"><a href="#fn:8" class="footnote-ref" role="doc-noteref">8</a></sup>, <sup id="fnref:22"><a href="#fn:22" class="footnote-ref" role="doc-noteref">22</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/actor-model-gateway-server.webp" title="Figure 18: Gateway server; Client connection management using the actor model" data-thumbnail="/live-comment-system-design/actor-model-gateway-server.webp" data-sub-html="<h2>Figure 18: Gateway server; Client connection management using the actor model</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/actor-model-gateway-server.webp"
            data-srcset="/live-comment-system-design/actor-model-gateway-server.webp, /live-comment-system-design/actor-model-gateway-server.webp 1.5x, /live-comment-system-design/actor-model-gateway-server.webp 2x"
            data-sizes="auto"
            alt="Figure 18: Gateway server; Client connection management using the actor model" width="1206" height="887" />
    </a><figcaption class="image-caption">Figure 18: Gateway server; Client connection management using the actor model</figcaption>
    </figure>
<p>An SSE persistent client connection can be assigned to an actor. When an actor receives a message, the handler on the actor defines how the message will be published to the client connection. The actor-style programming can be implemented with actor frameworks such as <a href="https://akka.io/" target="_blank" rel="noopener noreffer ">Akka</a> (Java, Scala), <a href="https://pykka.readthedocs.io/en/latest/index.html" target="_blank" rel="noopener noreffer ">Pykka</a> (Python), or <a href="https://cloudi.org/" target="_blank" rel="noopener noreffer ">Cloudi</a> (Erlang) for building highly concurrent, distributed, and resilient message-driven applications <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>, <sup id="fnref:8"><a href="#fn:8" class="footnote-ref" role="doc-noteref">8</a></sup>, <sup id="fnref:22"><a href="#fn:22" class="footnote-ref" role="doc-noteref">22</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/actor-model-broadcast.webp" title="Figure 19: Gateway server; Broadcasting live comments using the actor model" data-thumbnail="/live-comment-system-design/actor-model-broadcast.webp" data-sub-html="<h2>Figure 19: Gateway server; Broadcasting live comments using the actor model</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/actor-model-broadcast.webp"
            data-srcset="/live-comment-system-design/actor-model-broadcast.webp, /live-comment-system-design/actor-model-broadcast.webp 1.5x, /live-comment-system-design/actor-model-broadcast.webp 2x"
            data-sizes="auto"
            alt="Figure 19: Gateway server; Broadcasting live comments using the actor model" width="1515" height="1052" />
    </a><figcaption class="image-caption">Figure 19: Gateway server; Broadcasting live comments using the actor model</figcaption>
    </figure>
<p>The (child) actors are managed by the supervisor actor. The following operations are executed when a live comment is published by the dispatcher <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ol>
<li>the dispatcher publishes a live comment to the supervisor actor in the gateway server over the HTTP</li>
<li>the supervisor actor broadcasts the live comment to all the child actors</li>
<li>child actors use the connection handle to forward the live comment to the receivers</li>
</ol>
<p>The client (receiver) will render the received live comment on the web browser.</p>
<hr>
<hr>
<h3 id="how-to-handle-live-comments-on-multiple-livevideos">How to Handle Live Comments on Multiple Live Videos?</h3>
<p>There might be multiple clients watching distinct live videos connected to the same gateway server. The actors on the gateway server should not publish the live comment on a specific live video to all the connected clients because some of the clients might be watching a different live video. In Figure 20, clients connected to the same gateway server are watching distinct live videos with live video IDs <em>blue</em> and <em>red</em>. When the client publishes a live comment on the live video with <em>blue</em> as the ID, only the clients watching the live video with <em>blue</em> as the ID should be able to view the published live comment.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/live-comment-multiple-videos.webp" title="Figure 20: Gateway server; Publishing live comments on multiple live videos using a subscription" data-thumbnail="/live-comment-system-design/live-comment-multiple-videos.webp" data-sub-html="<h2>Figure 20: Gateway server; Publishing live comments on multiple live videos using a subscription</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/live-comment-multiple-videos.webp"
            data-srcset="/live-comment-system-design/live-comment-multiple-videos.webp, /live-comment-system-design/live-comment-multiple-videos.webp 1.5x, /live-comment-system-design/live-comment-multiple-videos.webp 2x"
            data-sizes="auto"
            alt="Figure 20: Gateway server; Publishing live comments on multiple live videos using a subscription" width="2520" height="1565" />
    </a><figcaption class="image-caption">Figure 20: Gateway server; Publishing live comments on multiple live videos using a subscription</figcaption>
    </figure>
<p>An in-memory subscription store using Redis can be provisioned to run on the same machine where the gateway server runs. The client starting to watch a live video subscribes to the specific live video on the gateway server over HTTP. The <strong>subscription store</strong> will keep an in-memory mapping between a live video and the set of receivers. An in-memory subscription store is used by the gateway server for storing viewership associativity for the following reasons <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ul>
<li>subscription data is local to clients connected to the gateway server</li>
<li>the client connections are strongly tied to the lifecycle of the gateway server</li>
</ul>
<p>In layman&rsquo;s terms, an in-memory subscription store is used to identify the live video watched by each client connected to the gateway server. The following operations are executed when a client publishes a live comment on the live video with <em>blue</em> as the ID <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ol>
<li>the dispatcher publishes a live comment to the supervisor actor in the gateway server over the HTTP</li>
<li>the supervisor actor queries the local in-memory subscription store to identify the clients subscribed to the live video with <em>blue</em> as the ID</li>
<li>the supervisor actor broadcasts the live comment to all the subscribed child actors</li>
<li>child actors use the connection handle to forward the live comment to the receivers</li>
</ol>
<p>The local in-memory subscription store allows the efficient publishing of live comments on multiple live videos.</p>
<hr>
<hr>
<h3 id="how-to-support-massive-concurrent-clients-on-multiple-livevideos">How to Support Massive Concurrent Clients on Multiple Live Videos?</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/dispatcher-broadcast-live-comments.webp" title="Figure 21: Dispatcher broadcasting live comments between gateway servers" data-thumbnail="/live-comment-system-design/dispatcher-broadcast-live-comments.webp" data-sub-html="<h2>Figure 21: Dispatcher broadcasting live comments between gateway servers</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/dispatcher-broadcast-live-comments.webp"
            data-srcset="/live-comment-system-design/dispatcher-broadcast-live-comments.webp, /live-comment-system-design/dispatcher-broadcast-live-comments.webp 1.5x, /live-comment-system-design/dispatcher-broadcast-live-comments.webp 2x"
            data-sizes="auto"
            alt="Figure 21: Dispatcher broadcasting live comments between gateway servers" width="2211" height="1436" />
    </a><figcaption class="image-caption">Figure 21: Dispatcher broadcasting live comments between gateway servers</figcaption>
    </figure>
<p>The gateway server should be partitioned by provisioning multiple instances of gateway servers to handle massive concurrent clients as shown in Figure 21. An additional service known as the <strong>dispatcher</strong> is provisioned to broadcast live comments between multiple gateway servers <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>. The clients connected to a gateway server might be watching only a subset of live videos. It is inefficient for the dispatcher to broadcast the live comments on all live videos to all gateway servers because some gateway servers might be subscribed to only a subset of live videos.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/dispatcher-broadcast-all-gateway.webp" title="Figure 22: Dispatcher broadcasting live comments to all gateway servers" data-thumbnail="/live-comment-system-design/dispatcher-broadcast-all-gateway.webp" data-sub-html="<h2>Figure 22: Dispatcher broadcasting live comments to all gateway servers</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/dispatcher-broadcast-all-gateway.webp"
            data-srcset="/live-comment-system-design/dispatcher-broadcast-all-gateway.webp, /live-comment-system-design/dispatcher-broadcast-all-gateway.webp 1.5x, /live-comment-system-design/dispatcher-broadcast-all-gateway.webp 2x"
            data-sizes="auto"
            alt="Figure 22: Dispatcher broadcasting live comments to all gateway servers" width="2211" height="1436" />
    </a><figcaption class="image-caption">Figure 22: Dispatcher broadcasting live comments to all gateway servers</figcaption>
    </figure>
<p>For example, in Figure 22, clients connected to gateway server 1 are only watching the live video with <em>blue</em> as the video ID. When the dispatcher publishes live comments on live video with <em>red</em> as the video ID to the gateway server 1, the published live comments will be processed and subsequently ignored by the gateway server 1 resulting in wasted computing resources. The dispatcher should not publish live comments on all live videos to all the gateway servers for improved performance <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/gateway-server-subscribe-endpoint-store.webp" title="Figure 23: Gateway servers subscribing to an in-memory endpoint store in the dispatcher" data-thumbnail="/live-comment-system-design/gateway-server-subscribe-endpoint-store.webp" data-sub-html="<h2>Figure 23: Gateway servers subscribing to an in-memory endpoint store in the dispatcher</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/gateway-server-subscribe-endpoint-store.webp"
            data-srcset="/live-comment-system-design/gateway-server-subscribe-endpoint-store.webp, /live-comment-system-design/gateway-server-subscribe-endpoint-store.webp 1.5x, /live-comment-system-design/gateway-server-subscribe-endpoint-store.webp 2x"
            data-sizes="auto"
            alt="Figure 23: Gateway servers subscribing to an in-memory endpoint store in the dispatcher" width="2653" height="1284" />
    </a><figcaption class="image-caption">Figure 23: Gateway servers subscribing to an in-memory endpoint store in the dispatcher</figcaption>
    </figure>
<p>An in-memory endpoint store using Redis can be provisioned to run locally on the same machine as the dispatcher. The client starting to watch a live video subscribes on the gateway server over HTTP. The subscription store in the gateway server will keep the mapping between the specific live video and the set of receivers. The gateway server will subscribe to the specific live video on the dispatcher over HTTP. An in-memory <strong>endpoint store</strong> on the dispatcher will keep the mapping between a live video and the set of subscribed gateway servers. An in-memory endpoint store is used by the dispatcher for keeping the subscription data of the gateway servers due to the following reasons <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ul>
<li>efficiently dispatch live comments across multiple gateway servers</li>
<li>identify the gateway servers subscribed to a particular live video</li>
</ul>
<p>In layman&rsquo;s terms, an in-memory endpoint store in the dispatcher is used to identify the live video subscription by the gateway servers for improved performance.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/dispatcher-publish-subscribed-gateway-servers.webp" title="Figure 24: Dispatcher publishing live comments to the subscribed gateways servers" data-thumbnail="/live-comment-system-design/dispatcher-publish-subscribed-gateway-servers.webp" data-sub-html="<h2>Figure 24: Dispatcher publishing live comments to the subscribed gateways servers</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/dispatcher-publish-subscribed-gateway-servers.webp"
            data-srcset="/live-comment-system-design/dispatcher-publish-subscribed-gateway-servers.webp, /live-comment-system-design/dispatcher-publish-subscribed-gateway-servers.webp 1.5x, /live-comment-system-design/dispatcher-publish-subscribed-gateway-servers.webp 2x"
            data-sizes="auto"
            alt="Figure 24: Dispatcher publishing live comments to the subscribed gateways servers" width="3008" height="1537" />
    </a><figcaption class="image-caption">Figure 24: Dispatcher publishing live comments to the subscribed gateways servers</figcaption>
    </figure>
<p>The following operations ate executed when the client publishes a live comment on the live video with <em>red</em> as the ID <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ol>
<li>the dispatcher queries the local in-memory endpoint store to identify the subscribed gateway servers on the live video with <em>red</em> as the ID</li>
<li>the dispatcher publishes the live comment to the subscribed gateway servers over HTTP</li>
<li>the gateway server queries the local in-memory subscription store to identify the clients subscribed to the live video with <em>red</em> as the ID</li>
<li>the gateway server broadcasts the live comment to all the subscribed clients through SSE</li>
</ol>
<p>The major drawback of the current architecture is that the dispatcher becomes the bottleneck for scalability. The performance of the live comment will degrade on peak load because there is only a single instance of dispatcher deployed.</p>
<hr>
<hr>
<h3 id="scaling-live-comments-to-handle-peakload">Scaling Live Comments to Handle Peak Load</h3>
<p>When an extremely high rate of live comments is published per second, the single instance of dispatcher will degrade the overall performance of the live comment service. The dispatcher can be replicated for horizontal scaling. The dispatcher should be made stateless for replicating the dispatcher. The dispatcher can become stateless by moving out gateway subscription state to an external store.</p>
<p>A disk-based key-value store known as the <strong>endpoint store</strong> can persist the subscription associations between a live video and the set of gateway servers. The endpoint store must be replicated for scalability and high availability. The endpoint store should only persist the gateway server subscription local to the data center. DynamoDB, Redis, or MongoDB can be used to provision the endpoint store. In addition, the endpoint store provides durability in case the dispatcher fails. Any dispatcher will be able to identify the set of subscribed gateway servers by querying the external endpoint store <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/scaling-dispatcher.webp" title="Figure 25: Scaling the dispatcher using a key-value endpoint store" data-thumbnail="/live-comment-system-design/scaling-dispatcher.webp" data-sub-html="<h2>Figure 25: Scaling the dispatcher using a key-value endpoint store</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/scaling-dispatcher.webp"
            data-srcset="/live-comment-system-design/scaling-dispatcher.webp, /live-comment-system-design/scaling-dispatcher.webp 1.5x, /live-comment-system-design/scaling-dispatcher.webp 2x"
            data-sizes="auto"
            alt="Figure 25: Scaling the dispatcher using a key-value endpoint store" width="3008" height="1537" />
    </a><figcaption class="image-caption">Figure 25: Scaling the dispatcher using a key-value endpoint store</figcaption>
    </figure>
<p>The dispatcher communicates with gateway servers over HTTP. Any dispatcher instance will be able to publish live comments to any gateway server. The following operations are executed when the client publishes a live comment on a live video with <em>red</em> as the ID <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ol>
<li>the dispatcher independently queries the external endpoint store to identify the set of subscribed gateway servers on the live video with <em>red</em> as the ID</li>
<li>the dispatcher publishes the live comment to the set of subscribed gateway servers over HTTP</li>
<li>the gateway server queries the local in-memory subscription store to identify the clients subscribed to the live video with <em>red</em> as the ID</li>
<li>the gateway server broadcasts the live comment to all the subscribed clients through SSE</li>
</ol>
<p>The endpoint store doesn&rsquo;t need to be partitioned due to the small dataset.</p>
<hr>
<hr>
<h3 id="what-is-the-subscribe-workflow-and-publish-workflow-for-live-comments">What Is the Subscribe Workflow and Publish Workflow for Live Comments?</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/subscribe-workflow.webp" title="Figure 26: Live comment; Subscribe workflow when the client starts watching a live video" data-thumbnail="/live-comment-system-design/subscribe-workflow.webp" data-sub-html="<h2>Figure 26: Live comment; Subscribe workflow when the client starts watching a live video</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/subscribe-workflow.webp"
            data-srcset="/live-comment-system-design/subscribe-workflow.webp, /live-comment-system-design/subscribe-workflow.webp 1.5x, /live-comment-system-design/subscribe-workflow.webp 2x"
            data-sizes="auto"
            alt="Figure 26: Live comment; Subscribe workflow when the client starts watching a live video" width="1661" height="1148" />
    </a><figcaption class="image-caption">Figure 26: Live comment; Subscribe workflow when the client starts watching a live video</figcaption>
    </figure>
<p>The following operations are executed for the subscription when the client starts watching a live video <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ol>
<li>the client subscribes to the gateway server over HTTP</li>
<li>the gateway stores the viewership associations on the in-memory subscription store</li>
<li>the gateway server makes a subscription request on the endpoint store by creating an entry on the key-value store</li>
</ol>
<figure><a class="lightgallery" href="/live-comment-system-design/publish-workflow.webp" title="Figure 27: Live comment; Publish workflow when the client publishes a live comment" data-thumbnail="/live-comment-system-design/publish-workflow.webp" data-sub-html="<h2>Figure 27: Live comment; Publish workflow when the client publishes a live comment</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/publish-workflow.webp"
            data-srcset="/live-comment-system-design/publish-workflow.webp, /live-comment-system-design/publish-workflow.webp 1.5x, /live-comment-system-design/publish-workflow.webp 2x"
            data-sizes="auto"
            alt="Figure 27: Live comment; Publish workflow when the client publishes a live comment" width="2687" height="1772" />
    </a><figcaption class="image-caption">Figure 27: Live comment; Publish workflow when the client publishes a live comment</figcaption>
    </figure>
<p>The following operations are executed when the client publishes  a live comment on the live video <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ol>
<li>the load balancer distributes the live comment published by the client to any random dispatcher over HTTP</li>
<li>the dispatcher queries the endpoint store for identifying the set of gateway servers subscribed to the particular live video</li>
<li>the dispatcher forwards the live comment to the set of subscribed gateway servers over the HTTP</li>
<li>the gateway servers query the local in-memory subscription store to identify the subscribed clients for the particular live video</li>
<li>the gateway servers broadcast the live comments to the subscribed clients over SSE</li>
</ol>
<hr>
<hr>
<h3 id="how-to-deploy-the-live-comment-service-across-multiple-datacenters">How to Deploy the Live Comment Service Across Multiple Data Centers?</h3>
<p>The live comment service should be deployed across multiple data centers (<strong>DC</strong>) for scalability and performance. There might be a scenario when no clients are watching a particular live video on some data centers. The dispatcher can query the local endpoint store to identify if there are any gateway servers subscribed to the particular live video in the local data center. The endpoint store keeps only gateway servers subscription local to the data center <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>.</p>
<p>For instance, in Figure 28, no clients are watching the video with <em>red</em> as the video ID on data centers one and three. When a client publishes a live comment on the live video with <em>red</em> as the video ID, only the dispatcher on data center two should broadcast the live comment to the subscribed gateway servers. The dispatcher must broadcast the live comment to the dispatchers in peer data centers for ensuring that the clients subscribed to the particular live video on peer data centers receives the live comment in real-time.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/live-comment-cross-data-center.webp" title="Figure 28: Publishing the live comment across data centers through broadcasting" data-thumbnail="/live-comment-system-design/live-comment-cross-data-center.webp" data-sub-html="<h2>Figure 28: Publishing the live comment across data centers through broadcasting</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/live-comment-cross-data-center.webp"
            data-srcset="/live-comment-system-design/live-comment-cross-data-center.webp, /live-comment-system-design/live-comment-cross-data-center.webp 1.5x, /live-comment-system-design/live-comment-cross-data-center.webp 2x"
            data-sizes="auto"
            alt="Figure 28: Publishing the live comment across data centers through broadcasting" width="3532" height="1848" />
    </a><figcaption class="image-caption">Figure 28: Publishing the live comment across data centers through broadcasting</figcaption>
    </figure>
<p>The following operations are executed when the client publishes a live comment on the live video <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>:</p>
<ol>
<li><a href="https://en.wikipedia.org/wiki/GeoDNS" target="_blank" rel="noopener noreffer ">GeoDNS</a> routes the live comment to a dispatcher on the data center closest to the client</li>
<li>the dispatcher broadcasts the live comment to dispatchers on peer data centers over HTTP</li>
<li>the dispatcher queries the local endpoint store to check if there are any subscribed gateway servers on the particular live video</li>
<li>the subscribed gateway server queries the local in-memory subscription store to identify the subscribed clients</li>
<li>the gateway server fans out the live comment to the subscribed clients over SSE</li>
</ol>
<p>The current architecture is scalable and will satisfy the requirements of live comment system design. The alternative approaches for implementing cross-data center live comment service are the following:</p>
<ul>
<li>cross-data center subscription</li>
<li>endpoint store configured for the entire region</li>
</ul>
<p>When the client starts watching a live video, the gateway server could perform a cross-data center subscription. However, the client connections are ephemeral because the client might scroll through the Facebook newsfeed, and the gateway servers subscription should be frequently updated across cross-data centers causing poor performance and degraded latency.</p>
<p>Alternatively, the endpoint store can be configured globally for keeping gateway servers subscriptions across multiple data centers covering an entire region. When a key-value store such as DynamoDB is used for the endpoint store, the <a href="https://en.wikipedia.org/wiki/Quorum_%28distributed_computing%29" target="_blank" rel="noopener noreffer ">quorum</a> should be tuned to higher <a href="https://systemdesign.one/consistency-patterns/" target="_blank" rel="noopener noreffer ">consistency</a> to ensure that all subscribed gateway servers will receive live comments. However, the global endpoint store will result in some clients not receiving the live comments due to eventual consistency and also cause poor latency.</p>
<hr>
<hr>
<h3 id="how-to-support-the-typing-indicators-on-livevideo">How to Support the Typing Indicators on Live Video?</h3>
<p>The JavaScript logic using <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/addEventListener" target="_blank" rel="noopener noreffer ">event listeners</a> on the client can trigger events when the user starts typing a live comment. The real-time platform can broadcast the typing event to the subscribed clients. The web browser on the subscribed clients can display the typing indicator.</p>
<hr>
<hr>
<h3 id="how-to-display-the-total-count-of-comments-on-each-livevideo">How to Display the Total Count of Comments on Each Live Video?</h3>
<p><a href="https://en.wikipedia.org/wiki/HyperLogLog" target="_blank" rel="noopener noreffer ">HyperLogLog</a> can be used to show the approximate count of total comments on each live video in a space-efficient manner. <a href="https://redis.io/docs/data-types/hyperloglogs/" target="_blank" rel="noopener noreffer ">Redis</a> offers out-of-the-box support for HyperLogLog probabilistic data structure.</p>
<hr>
<hr>
<hr>
<p><strong>Download my system design playbook for free on newsletter signup:</strong></p>
<div class="newsletter-container">
<iframe loading="lazy" class="newsletter-responsive-iframe" title="System Design Newsletter" src="https://newsletter.systemdesign.one/embed" scrolling="no"></iframe>
</div>
<hr>
<hr>
<hr>
<h3 id="scalability">Scalability</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/live-comment-performance.webp" title="Figure 29: Performance of live comment distribution" data-thumbnail="/live-comment-system-design/live-comment-performance.webp" data-sub-html="<h2>Figure 29: Performance of live comment distribution</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/live-comment-performance.webp"
            data-srcset="/live-comment-system-design/live-comment-performance.webp, /live-comment-system-design/live-comment-performance.webp 1.5x, /live-comment-system-design/live-comment-performance.webp 2x"
            data-sizes="auto"
            alt="Figure 29: Performance of live comment distribution" width="2211" height="1436" />
    </a><figcaption class="image-caption">Figure 29: Performance of live comment distribution</figcaption>
    </figure>
<p>The dispatcher provisioned on a modern server can handle up to 5000 requests per second. A modern gateway server implemented with the actor model can hold up to 100 thousand concurrent SSE client connections. The multiplication factor introduced by the combination of the dispatcher and gateway servers makes the live commenting service extremely scalable. Another multiplication factor is within the gateway server using the <a href="https://newsletter.systemdesign.one/p/actor-model" target="_blank" rel="noopener noreffer ">actor model</a>. The actor model allows the reuse of a pool of threads for increased throughput. The live comment service can be horizontally scaled by provisioning new servers <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>, <sup id="fnref:22"><a href="#fn:22" class="footnote-ref" role="doc-noteref">22</a></sup>, <sup id="fnref:2"><a href="#fn:2" class="footnote-ref" role="doc-noteref">2</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/load-balance-gateway-servers.webp" title="Figure 30: Load balancing client connection to the gateway servers" data-thumbnail="/live-comment-system-design/load-balance-gateway-servers.webp" data-sub-html="<h2>Figure 30: Load balancing client connection to the gateway servers</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/load-balance-gateway-servers.webp"
            data-srcset="/live-comment-system-design/load-balance-gateway-servers.webp, /live-comment-system-design/load-balance-gateway-servers.webp 1.5x, /live-comment-system-design/load-balance-gateway-servers.webp 2x"
            data-sizes="auto"
            alt="Figure 30: Load balancing client connection to the gateway servers" width="1874" height="1107" />
    </a><figcaption class="image-caption">Figure 30: Load balancing client connection to the gateway servers</figcaption>
    </figure>
<p>Load balancers should be introduced between different layers of the system for scalability and improved fault tolerance. The services should utilize the full capacity of the servers for scalability <sup id="fnref:23"><a href="#fn:23" class="footnote-ref" role="doc-noteref">23</a></sup>. Autoscaling can be enabled to meet elastic demand and deal with potential traffic spikes. Horizontal scaling comes with the tradeoff of a complex architecture, and increased infrastructure and maintenance costs <sup id="fnref:4"><a href="#fn:4" class="footnote-ref" role="doc-noteref">4</a></sup>.</p>
<hr>
<hr>
<h3 id="latency">Latency</h3>
<p>The live comment service is deployed on multiple data centers to keep the clients closer to the servers to improve the latency. <a href="https://samza.apache.org/" target="_blank" rel="noopener noreffer ">Apache Samza</a> can be used to measure the end-to-end latency of the live comment microservice system <sup id="fnref:24"><a href="#fn:24" class="footnote-ref" role="doc-noteref">24</a></sup>. The latency of the live comment service is quite low due to the following reasons <sup id="fnref:4"><a href="#fn:4" class="footnote-ref" role="doc-noteref">4</a></sup>, <sup id="fnref:7"><a href="#fn:7" class="footnote-ref" role="doc-noteref">7</a></sup>, <sup id="fnref:23"><a href="#fn:23" class="footnote-ref" role="doc-noteref">23</a></sup>:</p>
<ul>
<li>there is only one key value lookup from the (disk-based) endpoint store</li>
<li>there is only one in-memory lookup from the subscription store</li>
<li>the network hops are very few</li>
</ul>
<p>Additionally, Redis can be configured in the cache-aside pattern for improving the performance of endpoint store lookups because frequent disk I/O might become a bottleneck. The TTL on the Redis cache must be set to a reasonably short timeframe for cache invalidation.</p>
<hr>
<hr>
<h3 id="concurrency">Concurrency</h3>
<p>The gateway servers can be tuned to improve the count of concurrent connections supported by the server. The following operations could be performed to improve the performance of the gateway servers <sup id="fnref:8"><a href="#fn:8" class="footnote-ref" role="doc-noteref">8</a></sup>, <sup id="fnref:23"><a href="#fn:23" class="footnote-ref" role="doc-noteref">23</a></sup>:</p>
<ul>
<li>increase the thread count limit by decreasing the stack size per thread</li>
<li>increase the thread count limit by decreasing the memory allocated to the heap</li>
<li>increase the limit on the number of open connections between the load balancer and the server</li>
<li>increase the per-process file descriptor limit</li>
<li>modify the kernel parameter to increase the size of the backlog of TCP connections accepted by the server</li>
</ul>
<p>The <a href="https://en.wikipedia.org/wiki/Readers%E2%80%93writer_lock" target="_blank" rel="noopener noreffer ">readers-writer lock</a> can be used for updating the set of gateway servers subscribed to a particular live video on the endpoint store. The client subscribing to a gateway server is handled by the in-memory subscription store running on Redis. The concurrency problems are automatically solved because the operations on Redis are atomic and single-threaded <sup id="fnref:5"><a href="#fn:5" class="footnote-ref" role="doc-noteref">5</a></sup>.</p>
<hr>
<hr>
<h3 id="high-availability">High Availability</h3>
<figure><a class="lightgallery" href="/live-comment-system-design/traffic-pattern-live-comment.webp" title="Figure 31: The traffic pattern for live comments on a live video" data-thumbnail="/live-comment-system-design/traffic-pattern-live-comment.webp" data-sub-html="<h2>Figure 31: The traffic pattern for live comments on a live video</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/traffic-pattern-live-comment.webp"
            data-srcset="/live-comment-system-design/traffic-pattern-live-comment.webp, /live-comment-system-design/traffic-pattern-live-comment.webp 1.5x, /live-comment-system-design/traffic-pattern-live-comment.webp 2x"
            data-sizes="auto"
            alt="Figure 31: The traffic pattern for live comments on a live video" width="1425" height="788" />
    </a><figcaption class="image-caption">Figure 31: The traffic pattern for live comments on a live video</figcaption>
    </figure>
<p>The traffic pattern for live comments on extremely popular live videos  is usually very steep from the initial phase until the end of the live video and the traffic subsequently drops to the floor. In layman&rsquo;s terms, the traffic pattern for live comments on a live video is spiky. When the live video becomes extremely popular, the number of concurrent clients subscribed for viewing the live comments increases causing the <a href="https://en.wikipedia.org/wiki/Thundering_herd_problem" target="_blank" rel="noopener noreffer ">thundering herd problem</a> <sup id="fnref:2"><a href="#fn:2" class="footnote-ref" role="doc-noteref">2</a></sup>. The thundering herd problem from live comments on the live video can be resolved by the following operations <sup id="fnref:13"><a href="#fn:13" class="footnote-ref" role="doc-noteref">13</a></sup>, <sup id="fnref:2"><a href="#fn:2" class="footnote-ref" role="doc-noteref">2</a></sup>:</p>
<ul>
<li>load balancer redirects the clients to gateway servers with free capacity</li>
<li>include jitter on client reconnection logic</li>
<li>implement backpressure and exponential backoff on services</li>
<li>concurrent clients can perform request coalescing for fetching older comments</li>
<li>predicting the load before hitting the service limits</li>
</ul>
<p>The reconnection storms on client SSE connections to the gateway servers on deployment and scale-down events can be prevented by configuring the deployment to keep the existing servers for a few hours after the termination to allow the majority of the existing client SSE connections to close naturally. The tradeoff with the approach is relatively slower deployments <sup id="fnref:3"><a href="#fn:3" class="footnote-ref" role="doc-noteref">3</a></sup>. The services should operate in hot-hot mode with failover for high availability. The read-write operations can be handled by both instances for scalability <sup id="fnref:10"><a href="#fn:10" class="footnote-ref" role="doc-noteref">10</a></sup>.</p>
<hr>
<hr>
<h3 id="fault-tolerance">Fault Tolerance</h3>
<p>The live comment service should be load tested for identifying failures in capacity estimation <sup id="fnref:2"><a href="#fn:2" class="footnote-ref" role="doc-noteref">2</a></sup>. Rate limiters can be used to reduce the load from the service on service degradation or traffic overloading <sup id="fnref:3"><a href="#fn:3" class="footnote-ref" role="doc-noteref">3</a></sup>. A high level of observability through monitoring and alerting should be configured on all the services <sup id="fnref:3"><a href="#fn:3" class="footnote-ref" role="doc-noteref">3</a></sup>, <sup id="fnref:23"><a href="#fn:23" class="footnote-ref" role="doc-noteref">23</a></sup>. The live comment service must be deployed across multiple data centers in the same region and across multiple regions across the globe for improved fault tolerance. The deployment of the live comment service on multiple data centers across the globe comes with the challenge of increased engineering, DevOps efforts, and infrastructure costs <sup id="fnref:4"><a href="#fn:4" class="footnote-ref" role="doc-noteref">4</a></sup>.</p>
<hr>
<hr>
<h3 id="development-and-deployment">Development and Deployment</h3>
<p>Infrastructure as Code can be used to provision servers on bare metal. Continuous integration and continuous delivery/continuous deployment (<strong>CI/CD</strong>) should be set up for faster development cycles <sup id="fnref:23"><a href="#fn:23" class="footnote-ref" role="doc-noteref">23</a></sup>.</p>
<hr>
<hr>
<h3 id="durability">Durability</h3>
<p>The live comments should be persisted in a NoSQL database such as Apache Cassandra for durability. The compaction process in Apache Cassandra for removing the tombstones can become a potential performance bottleneck over time. The database can be configured in different clusters for different profiles of data usage to overcome the limitation. For example, different replication rules can be set for different regulatory requirements (HIPAA, GDPR). Database snapshots must be taken at periodic intervals for data recovery. The data can be replicated on multiple hyper scalers within the same region for durability <sup id="fnref:10"><a href="#fn:10" class="footnote-ref" role="doc-noteref">10</a></sup>.</p>
<figure><a class="lightgallery" href="/live-comment-system-design/live-comment-persistent-storage.webp" title="Figure 32: Storing live comments in the NoSQL database" data-thumbnail="/live-comment-system-design/live-comment-persistent-storage.webp" data-sub-html="<h2>Figure 32: Storing live comments in the NoSQL database</h2>">
        <img
            class="lazyload"
            src="/svg/loading.min.svg"
            data-src="/live-comment-system-design/live-comment-persistent-storage.webp"
            data-srcset="/live-comment-system-design/live-comment-persistent-storage.webp, /live-comment-system-design/live-comment-persistent-storage.webp 1.5x, /live-comment-system-design/live-comment-persistent-storage.webp 2x"
            data-sizes="auto"
            alt="Figure 32: Storing live comments in the NoSQL database" width="2687" height="1772" />
    </a><figcaption class="image-caption">Figure 32: Storing live comments in the NoSQL database</figcaption>
    </figure>
<p>The following operations are executed when the client publishes a live comment on the live video:</p>
<ol>
<li>the load balancer distributes the live comment published by the client to any random dispatcher over HTTP</li>
<li>the dispatcher writes the live comment on the comment store (NoSQL) for persistent storage</li>
<li>the dispatcher queries the endpoint store for identifying the set of gateway servers subscribed to the particular live video</li>
<li>the dispatcher forwards the live comment to the set of subscribed gateway servers over the HTTP</li>
<li>the gateway servers query the local in-memory subscription store to identify the subscribed clients for the particular live video</li>
<li>the gateway servers broadcast the live comments to the subscribed clients over SSE</li>
</ol>
<hr>
<hr>
<h3 id="operational-complexity">Operational Complexity</h3>
<p>Fully managed services hosted on hyper scalers like <a href="https://aws.amazon.com/" target="_blank" rel="noopener noreffer ">AWS</a> can reduce the operational complexity of the live comment service. Alternatively, the dispatcher in the live comment service can be implemented with serverless functions. The benefits of using serverless functions are the following <sup id="fnref:4"><a href="#fn:4" class="footnote-ref" role="doc-noteref">4</a></sup>:</p>
<ul>
<li>no infrastructure maintenance</li>
<li>reduced operational costs</li>
<li>scalability and availability</li>
<li>reduced latency</li>
</ul>
<hr>
<hr>
<hr>
<h2 id="summary">Summary</h2>
<p>The live comment is a popular system design interview question. The real-time platform built for publishing live comments can display Facebook reactions, likes, concurrent viewer counts, <a href="https://systemdesign.one/real-time-presence-platform-system-design/" target="_blank" rel="noopener noreffer ">user presence status</a>, online polls, or seen receipts.</p>
<hr>
<h2 id="what-to-learn-next">What to learn next?</h2>
<p><strong>Download my system design playbook for free on newsletter signup:</strong></p>
<div class="newsletter-container">
<iframe loading="lazy" class="newsletter-responsive-iframe" title="System Design Newsletter" src="https://newsletter.systemdesign.one/embed" scrolling="no"></iframe>
</div>
<hr>
<hr>
<hr>
<h2 id="license">License</h2>
<p><a href="https://creativecommons.org/licenses/by-nc-nd/4.0/" target="_blank" rel="noopener noreffer ">CC BY-NC-ND 4.0</a>: This license allows reusers to copy and distribute the content in this article in any medium or format in unadapted form only, for noncommercial purposes, and only so long as attribution is given to the creator. The original article must be backlinked.</p>
<hr>
<hr>
<hr>
<h2 id="references">References</h2>
<section class="footnotes" role="doc-endnotes">
<hr>
<ol>
<li id="fn:1" role="doc-endnote">
<p><a href="https://www.cloudflare.com/en-gb/learning/video/what-is-live-streaming/" target="_blank" rel="noopener noreffer ">What is live streaming?</a>, cloudflare.com&#160;<a href="#fnref:1" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:2" role="doc-endnote">
<p>Todd Hoff, <a href="http://highscalability.com/blog/2016/6/27/how-facebook-live-streams-to-800000-simultaneous-viewers.html" target="_blank" rel="noopener noreffer ">How Facebook Live Streams To 800,000 Simultaneous Viewers</a> (2016), highscalability.com&#160;<a href="#fnref:2" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:3" role="doc-endnote">
<p>Dima Zabello, Kyle Maxwell, and Saurabh Sharma, <a href="https://www.reddit.com/r/RedditEng/comments/pfgz4r/reddits_new_realtime_service/" target="_blank" rel="noopener noreffer ">Reddit&rsquo;s new real-time service</a>, reddit.com&#160;<a href="#fnref:3" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:4" role="doc-endnote">
<p>Matthew O&rsquo;Riordan, <a href="https://www.infoq.com/articles/serverless-websockets-realtime-messaging/" target="_blank" rel="noopener noreffer ">Using Serverless WebSockets to Enable Real-Time Messaging</a> (2022), infoq.com&#160;<a href="#fnref:4" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:5" role="doc-endnote">
<p>Fernando Doglio, <a href="https://www.memurai.com/blog/using-redis-for-chat-and-messaging" target="_blank" rel="noopener noreffer ">Using Redis for Chat and Messaging</a> (2022), memurai.com&#160;<a href="#fnref:5" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:6" role="doc-endnote">
<p>Matthew O&rsquo;Riordan, <a href="https://www.infoq.com/articles/realtime-event-driven-ecosystem/" target="_blank" rel="noopener noreffer ">The Challenges of Building a Reliable Real-Time Event-Driven Ecosystem</a> (2020), infoq.com&#160;<a href="#fnref:6" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:7" role="doc-endnote">
<p>Akhilesh Gupta, <a href="https://www.infoq.com/presentations/linkedin-play-akka-distributed-systems/" target="_blank" rel="noopener noreffer ">Streaming a Million Likes/Second: Real-Time Interactions on Live Video</a>, infoq.com&#160;<a href="#fnref:7" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:8" role="doc-endnote">
<p>Akhilesh Gupta, <a href="https://engineering.linkedin.com/blog/2016/10/instant-messaging-at-linkedin--scaling-to-hundreds-of-thousands-" target="_blank" rel="noopener noreffer ">Instant Messaging at LinkedIn: Scaling to Hundreds of Thousands of Persistent Connections on One Machine</a> (2016), engineering.linkedin.com&#160;<a href="#fnref:8" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:9" role="doc-endnote">
<p><a href="https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events" target="_blank" rel="noopener noreffer ">Using server-sent events</a>, mozilla.org&#160;<a href="#fnref:9" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:10" role="doc-endnote">
<p>Todd Greene, <a href="https://www.youtube.com/watch?v=twJji4DctcE" target="_blank" rel="noopener noreffer ">Efficiently Operating a Mass Real-time Data Infrastructure</a> (2020), AWS Events&#160;<a href="#fnref:10" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:11" role="doc-endnote">
<p>Ajeet Raina, <a href="https://developer.redis.com/howtos/chatapp/" target="_blank" rel="noopener noreffer ">How to build a Chat application using Redis</a>, developer.redis.com&#160;<a href="#fnref:11" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:12" role="doc-endnote">
<p>Ken Deeter, <a href="https://engineering.fb.com/2011/02/07/core-data/live-commenting-behind-the-scenes/" target="_blank" rel="noopener noreffer ">Live Commenting: Behind the Scenes</a> (2011), engineering.fb.com&#160;<a href="#fnref:12" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:13" role="doc-endnote">
<p>Jeff Barber, <a href="https://www.usenix.org/conference/srecon17americas/program/presentation/erlich" target="_blank" rel="noopener noreffer ">Building Real-Time Infrastructure at Facebook</a> (2017), usenix.org&#160;<a href="#fnref:13" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:14" role="doc-endnote">
<p><a href="https://www.amazon.com/Scalability-Startup-Engineers-Artur-Ejsmont/dp/0071843655" target="_blank" rel="noopener noreffer ">Web Scalability for Startup Engineers</a> by Artur Ejsmont (2015)&#160;<a href="#fnref:14" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:15" role="doc-endnote">
<p><a href="https://kafka.apache.org/documentation/#brokerconfigs_offsets.retention.minutes" target="_blank" rel="noopener noreffer ">Offsets retention minutes in Kafka</a>, kafka.apache.org&#160;<a href="#fnref:15" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:16" role="doc-endnote">
<p><a href="https://docs.confluent.io/kafka/operations-tools/manage-consumer-groups.html" target="_blank" rel="noopener noreffer ">Manage consumer groups in Kafka</a>, docs.confluent.io&#160;<a href="#fnref:16" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:17" role="doc-endnote">
<p><a href="https://redis.io/docs/data-types/streams/" target="_blank" rel="noopener noreffer ">Redis Streams</a>, redis.io&#160;<a href="#fnref:17" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:18" role="doc-endnote">
<p><a href="https://www.youtube.com/watch?v=Z8qcpXyMAiA" target="_blank" rel="noopener noreffer ">Redis Streams Explained</a> (2021), youtube.com&#160;<a href="#fnref:18" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:19" role="doc-endnote">
<p>Fernando Doglio, <a href="https://www.memurai.com/blog/apache-kafka-vs-redis-streams" target="_blank" rel="noopener noreffer ">Apache Kafka versus Redis Streams</a> (2021), memurai.com&#160;<a href="#fnref:19" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:20" role="doc-endnote">
<p><a href="https://redis.io/docs/management/persistence/" target="_blank" rel="noopener noreffer ">Redis persistence</a>, redis.io&#160;<a href="#fnref:20" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:21" role="doc-endnote">
<p>Antirez, <a href="http://antirez.com/news/114" target="_blank" rel="noopener noreffer ">Streams: a new general purpose data structure in Redis</a>, antirez.com&#160;<a href="#fnref:21" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:22" role="doc-endnote">
<p>Akhilesh Gupta, <a href="https://engineering.linkedin.com/blog/2018/01/now-you-see-me--now-you-dont--linkedins-real-time-presence-platf" target="_blank" rel="noopener noreffer ">Now You See Me, Now You Don&rsquo;t: LinkedIn&rsquo;s Real-Time Presence Platform</a> (2018), engineering.linkedin.com&#160;<a href="#fnref:22" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:23" role="doc-endnote">
<p>Todd Hoff, <a href="http://highscalability.com/blog/2010/3/16/justintvs-live-video-broadcasting-architecture.html" target="_blank" rel="noopener noreffer ">Justin.Tv&rsquo;s Live Video Broadcasting Architecture</a> (2010), highscalability.com&#160;<a href="#fnref:23" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
<li id="fn:24" role="doc-endnote">
<p>Max Wolffe, <a href="https://engineering.linkedin.com/blog/2018/04/samza-aeon--latency-insights-for-asynchronous-one-way-flows" target="_blank" rel="noopener noreffer ">Samza Aeon: Latency Insights for Asynchronous One-Way Flows</a> (2018), engineering.linkedin.com&#160;<a href="#fnref:24" class="footnote-backref" role="doc-backlink">&#x21a9;&#xfe0e;</a></p>
</li>
</ol>
</section></div><div class="post-footer" id="post-footer">
    <div class="post-info">
        <div class="post-info-line">
            <div class="post-info-mod">
                <span>Updated on 2023-05-18</span>
            </div><div class="post-info-license">
                <span>CC BY-NC 4.0</span>
            </div></div>
        <div class="post-info-line">
            <div class="post-info-md"></div>
            <div class="post-info-share">
                <span><a href="javascript:void(0);" title="Share on Twitter" data-sharer="twitter" data-url="https://systemdesign.one/live-comment-system-design/" data-title="Live Comment System Design" data-via="systemdesignone"><i class="fab fa-twitter fa-fw" aria-hidden="true"></i></a><a href="javascript:void(0);" title="Share on Facebook" data-sharer="facebook" data-url="https://systemdesign.one/live-comment-system-design/"><i class="fab fa-facebook-square fa-fw" aria-hidden="true"></i></a><a href="javascript:void(0);" title="Share on Linkedin" data-sharer="linkedin" data-url="https://systemdesign.one/live-comment-system-design/"><i class="fab fa-linkedin fa-fw" aria-hidden="true"></i></a><a href="javascript:void(0);" title="Share on WhatsApp" data-sharer="whatsapp" data-url="https://systemdesign.one/live-comment-system-design/" data-title="Live Comment System Design" data-web><i class="fab fa-whatsapp fa-fw" aria-hidden="true"></i></a><a href="javascript:void(0);" title="Share on Pinterest" data-sharer="pinterest" data-url="https://systemdesign.one/live-comment-system-design/" data-description="building the live commenting real-time platform" data-image="live-comment-system-design/live-comment.webp"><i class="fab fa-pinterest fa-fw" aria-hidden="true"></i></a><a href="javascript:void(0);" title="Share on Tumblr" data-sharer="tumblr" data-url="https://systemdesign.one/live-comment-system-design/" data-title="Live Comment System Design" data-caption="building the live commenting real-time platform"><i class="fab fa-tumblr fa-fw" aria-hidden="true"></i></a><a href="javascript:void(0);" title="Share on Hacker News" data-sharer="hackernews" data-url="https://systemdesign.one/live-comment-system-design/" data-title="Live Comment System Design"><i class="fab fa-hacker-news fa-fw" aria-hidden="true"></i></a><a href="javascript:void(0);" title="Share on Reddit" data-sharer="reddit" data-url="https://systemdesign.one/live-comment-system-design/"><i class="fab fa-reddit fa-fw" aria-hidden="true"></i></a><a href="javascript:void(0);" title="Share on VK" data-sharer="vk" data-url="https://systemdesign.one/live-comment-system-design/" data-title="Live Comment System Design" data-caption="building the live commenting real-time platform" data-image="live-comment-system-design/live-comment.webp"><i class="fab fa-vk fa-fw" aria-hidden="true"></i></a></span>
            </div>
        </div>
    </div>

    <div class="post-info-more">
        <section class="post-tags"></section>
        <section>
            <span><a href="javascript:void(0);" onclick="window.history.back();">Back</a></span>&nbsp;|&nbsp;<span><a href="/">Home</a></span>
        </section>
    </div>

    <div class="post-nav"><a href="/leaderboard-system-design/" class="prev" rel="prev" title="Leaderboard System Design"><i class="fas fa-angle-left fa-fw" aria-hidden="true"></i>Leaderboard System Design</a>
            <a href="/real-time-presence-platform-system-design/" class="next" rel="next" title="Real Time Presence Platform System Design">Real Time Presence Platform System Design<i class="fas fa-angle-right fa-fw" aria-hidden="true"></i></a></div>
</div>
<div id="comments"><div id="disqus_thread" class="comment"></div><noscript>
                Please enable JavaScript to view the comments powered by <a href="https://disqus.com/?ref_noscript">Disqus</a>.
            </noscript></div></article>


<div id="newsletter-overlay-id" class="newsletter-overlay">
    <a class="newsletter-overlay-closebutton" onclick="closeOverlay()">&times;</a>

    <div class="newsletter-overlay-content">
        <h1>Join the System Design Journey</h1>
        <h4>And download my system design playbook on newsletter signup for FREE:</h4>

        <iframe loading="lazy" class="overlay-newsletter-responsive-iframe" src="https://newsletter.systemdesign.one/embed" title="System Design Newsletter" width="480" height="320" frameborder="0" scrolling="no">
        </iframe>
    </div>
</div>

<script>
    const OVERLAY = "system_design_one_overlay";
    const TTL = 86400000; 
    const VERSION = "01";

    function closeOverlay() {
        setOverlayExpiry(OVERLAY, TTL, VERSION);
        document.getElementsByTagName('body')[0].style.overflow = "";
        document.getElementById("newsletter-overlay-id").style.display = "none";
    }

    function openOverlay() {
        document.getElementById("newsletter-overlay-id").style.display = "block";
        document.getElementsByTagName('body')[0].style.overflow = "hidden";
    }

    
    function setOverlayExpiry(key, ttl, version) {
        const now = new Date();
        const item = {
            version: version,
            expiry: now.getTime() + ttl,
        };
        localStorage.setItem(key, JSON.stringify(item));
    }

    function isOverlayExpired(key, version) {
        const itemStr = localStorage.getItem(key);
        
        if (!itemStr) {
            return true;
        }
        const item = JSON.parse(itemStr);
        const now = new Date();
        
        if (item.version !== version || now.getTime() > item.expiry) {
            
            
            localStorage.removeItem(key);
            return true;
        }
        return false;
    }

    window.onload = function () {
        if (isOverlayExpired(OVERLAY, VERSION) == true) {
            setTimeout(openOverlay, 60000); 
        }
    };

</script></div>
            </main><footer class="footer">
    <div class="footer-container"><div class="footer-line" itemscope itemtype="http://schema.org/CreativeWork"><i class="far fa-copyright fa-fw" aria-hidden="true"></i><span itemprop="copyrightYear">2022 - 2025</span><span class="author" itemprop="copyrightHolder">&nbsp;<a
                    href="https://www.linkedin.com/in/nk-systemdesign-one/" target="_blank">Neo Kim</a></span>&nbsp;|&nbsp;<span class="newsletter"><a rel="nofollow" href="https://newsletter.systemdesign.one/subscribe" target="_blank">Newsletter</a></span>&nbsp;|&nbsp;<span class="youtube"><a rel="nofollow" href="https://www.youtube.com/@systemdesignone?sub_confirmation=1" target="_blank">YouTube</a></span>&nbsp;|&nbsp;<span class="privacy"><a rel="nofollow" href="https://systemdesign.one/privacy-policy/" target="_blank">Privacy</a></span>&nbsp;|&nbsp;<span class="terms"><a rel="nofollow" href="https://systemdesign.one/terms-of-service/" target="_blank">Terms</a></span>&nbsp;|&nbsp;<span class="impressum"><a rel="nofollow" href="https://newsletter.systemdesign.one/p/impressum/" target="_blank">Impressum</a></span></div>
    </div>
</footer></div>

        <div id="fixed-buttons"><a href="#" id="back-to-top" class="fixed-button" title="Back to Top">
                <i class="fas fa-arrow-up fa-fw" aria-hidden="true"></i>
            </a><a href="#" id="view-comments" class="fixed-button" title="View Comments">
                <i class="fas fa-comment fa-fw" aria-hidden="true"></i>
            </a>
        </div><link rel="stylesheet" href="/lib/cookieconsent/cookieconsent.min.css"><link rel="stylesheet" href="/css/4e5f2e.min.css"><script type="text/javascript" src="https://systemdesign-one.disqus.com/embed.js" defer></script><script type="text/javascript" src="/lib/autocomplete/autocomplete.min.js"></script><script type="text/javascript" src="/lib/lunr/lunr.min.js"></script><script type="text/javascript" src="/lib/lazysizes/lazysizes.min.js"></script><script type="text/javascript" src="/lib/clipboard/clipboard.min.js"></script><script type="text/javascript" src="/lib/sharer/sharer.min.js"></script><script type="text/javascript" src="/lib/cookieconsent/cookieconsent.min.js"></script><script type="text/javascript">window.config={"code":{"copyTitle":"Copy to clipboard","maxShownLines":50},"comment":{},"cookieconsent":{"content":{"dismiss":"Got it!","href":"https://systemdesign.one/cookie-policy/","link":"Learn more","message":"This website uses cookies."},"enable":true,"palette":{"button":{"background":"#f0f0f0"},"popup":{"background":"#1aa3ff"}},"theme":"edgeless"},"search":{"highlightTag":"em","lunrIndexURL":"/index.json","maxResultLength":10,"noResultsFound":"No results found","snippetLength":30,"type":"lunr"}};</script><script type="text/javascript" src="/js/theme.min.js"></script><script type="text/javascript">
            window.dataLayer=window.dataLayer||[];function gtag(){dataLayer.push(arguments);}gtag('js', new Date());
            gtag('config', 'G-ELC84BW3LQ', { 'anonymize_ip': true });
        </script><script type="text/javascript" src="https://www.googletagmanager.com/gtag/js?id=G-ELC84BW3LQ" async></script></body>
</html>
