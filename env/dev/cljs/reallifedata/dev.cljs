(ns ^:figwheel-no-load reallifedata.dev
  (:require
    [reallifedata.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
