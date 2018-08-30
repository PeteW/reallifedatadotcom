(ns reallifedata.oss
  (:require [reagent.core :as reagent :refer [atom]]
            [cljsjs.react-xmasonry :as xmasonry]
            [clojure.set :as cs]
            [cljs.pprint :as pprint]
            [reallifedata.utils :refer [tile]]
            [reallifedata.appdb :refer [appdb]]))

(def XMasonry (reagent/adapt-react-class js/XMasonry))
(def XBlock (reagent/adapt-react-class js/XBlock))

(def projectfilter (atom "All"))

(def projects [
               {:title "SpaceVim"
                :description "The vim answer to spaceemacs, spacevim is a modern vim-as-an-ide distribution which manages plugins as layers, providing the ultimate vim configuration out of the box with consistent support in GUI or terminal"
                :description2 "This contribution is just having some fun, adding ascii art to splash screens. I support this project enthusastically but dont have any issues with it at the moment."
                :date "2018"
                :tech ["vim" "vim-script"]
                :project-url "https://github.com/SpaceVim/SpaceVim/pull/1440"
                :picture "https://camo.githubusercontent.com/73dd69d7837c503fb4690cfe6dbfc23305edf691/68747470733a2f2f737061636576696d2e6f72672f6c6f676f2e706e67"
                }
               {:title "ec2instances.info"
                :description "Enhance the ec2instances.info site with text-based column filtering enabling the application of multiple criteria to specific columns."
                :description2 "Selecting infrastructure is a fundamental optimization problem with 'hard' and 'soft' constraints. Finding the optimal choice is faster when you can apply your 'hard' constraints to each column and sort by the 'soft' constraints hence the contribution."
                :date "2019"
                :tech ["javascript" "aws"]
                :project-url "https://github.com/powdahound/ec2instances.info/pull/416"
                :picture "img/ec2instances.info.png"
                }
               {:title "txt24"
                :description "Personally one of my favorite projects to date. Queues up a collection of pre-authored messages and disperses them, via SMS or email, to multiple recipients over a pseudo-random schedule. Works as a slow-release photo album delivery."
                :description2 "Uses heroku-hosted mongodb for storage of messages. AWS s3 for images. Exposes a service endpoint which triggers sending cycles. For each cycle, for each distribution list, if a message has not been delivered for the distribution list within the cycle AND a random number criteria is met, messages are queued and delivered. I gave this to my wife as an anniversary gift, delivering one SMS image/message per day at random times for a full year. Used for various other events thereafter."
                :date "2016"
                :tech ["python" "aws" "heroku" "mongodb" "REST"]
                :project-url "https://github.com/PeteW/txt24"
                :picture "img/txt24.png"
                }
               {:title "Humane ORM"
                :description "ORM-generated SQL is designed to be performant but lacks human readability. This project is an attempt at lexing/parsing/rewriting the ORM-generated SQL into something less optimal but human readable for debugging purposes. It works with MOST queries generated by Entity Framework, NHibernate, python-SQLAlchemy, others may also work as well"
                :description2 "The lex/parse/generate support of this tool is complete, but, we must find a way to plug this into the processing pipeline of entity framework to bring it into action. This is not a completed project."
                :date "2014"
                :tech ["f#" "ORM"]
                :project-url "https://github.com/PeteW/HumaneOrm"
                :picture "img/entityframework.jpg"
                }
               {:title "MVC Perfmon"
                :description "Unobtrusely integrate custom perf counters into ASP.NET MVC by using action filter attributes"
                :description2 "This is a tool designed to expand the instrumentation of custom web applications for the benefit of ops team and alerting tools such as SolarWinds or PagerDuty."
                :date "2014"
                :tech ["c#" "mvc" "ops"]
                :project-url "https://github.com/PeteW/MvcPerfmon"
                :picture "https://raw.githubusercontent.com/PeteW/MvcPerfmon/master/Example.png"
                }
               {:title "Visual Studio Theme Gallery"
                :description "I have strong opinions when it comes to the UI/UX of my development environment. For those with similar feelings I created a shared gallery of exported settings for visual studio 2012 2013 and 2015 (and ostensibly working in 2017 as well)."
                :date "2013"
                :tech ["visual-studio"]
                :project-url "https://github.com/PeteW/VisualStudioThemes"
                :picture "https://raw.githubusercontent.com/PeteW/VisualStudioThemes/master/MintChocolate.png"
                }
               {:title "Zappa Framework - ALB integration"
                :description "Zappa enables zero-code-change conversion of UWSGI-compliant python applications into lambda functions. The framework takes care of all aspects of provisioning and deployment to AWS constructs on behalf of the user using CloudFormation and boto3 utilities."
                :description2 "This contribution adds full support for connecting zappa-lambda functions to application load balancers (ALB). Via configuration, load balancers, listeners, and target groups are set up and connected to lambda."
                :date "2019"
                :tech ["python" "aws" "REST"]
                :project-url "https://github.com/Miserlou/Zappa/pull/1807"
                :picture "https://camo.githubusercontent.com/7bdd37b656c15786839d868e1075cdf1926d4272/687474703a2f2f692e696d6775722e636f6d2f6f65506e484a6e2e6a7067"
                }
               {:title "NServiceBus Facade"
                :description "A starter implementation of NServiceBus REST interface enabling non-ESB clients to use the ODATA protocol, submit requests and poll for responses"
                :date "2015"
                :tech ["c#" "ESB" "REST"]
                :project-url "https://github.com/PeteW/nservicebusfacade"
                :picture "https://raw.githubusercontent.com/PeteW/NServiceBusFacade/master/DataFlow.png"
                }
               {:title "Luigi workflow framework - single task runner"
                :description "Hardedning support for timeout capabilities in single-task execution mode"
                :description2 "Luigi can run as single- or multi- process. This contrubution ensures support for hard-timeout limit feature is provided in either mode consistently"
                :date "2019"
                :tech ["python" "luigi"]
                :project-url "https://github.com/spotify/luigi/pull/2667"
                :picture "https://raw.githubusercontent.com/spotify/luigi/master/doc/luigi.png"
                }
               {:title "assembla2github"
                :description "A module which converts assembla tickets, milestones, and statuses into issues, milestones and issue states. Preserves ticket activity history."
                :date "2017"
                :description2 "Idempotent process. Flexible to mapping concepts between the two systems in different ways. Uses PyGithub library to talk to github public API"
                :tech ["python" "git"]
                :project-url "https://github.com/PeteW/assembla2github"
                }
               {:title "Luigi workflow framework - visualization"
                :description "Adding support for visualization of workflows from console"
                :description2 "A need to view (from the terminal) the series of tasks within a workflow and their dependencies and state. Instead of running luigi or viewing the web application, this contribution adds support for running a hypothetical execution of a workflow, enumerates all tasks and parameters in a recursive ascii-art tree visualization"
                :date "2017"
                :tech ["python" "luigi"]
                :project-url "https://github.com/spotify/luigi/pull/1680"
                :picture "https://raw.githubusercontent.com/spotify/luigi/master/doc/luigi.png"
                }
               {:title "SharePoint GWE"
                :description "A lightweight event-receiver-driven alternative to the windows workflow engine. Uses a custom lisp-based programming language to turn sharepoint lists into fully-capable workflow applications with no need for visual studio, windows workflow engine or biztalk"
                :description2 "Lisp-style language implemented using F# and lex/yacc parse and build AST for handling workflow logic. This allows users to write their own logic on the sharepoint list property page without the need for a development tool."
                :date "2011"
                :tech ["c#" "f#" "sharepoint" "DSL" "lisp"]
                :project-url "https://github.com/PeteW/sharepointgwe"
                :picture "https://raw.githubusercontent.com/PeteW/SharePointGwe/master/GweBehavior.png"
                }
               {:title "Sudoku wiz"
                :description "Native c++ app in windows phone store capable of not only solving sudoku puzzles but also detecting and reading the puzzles OCR from camera capture"
                :description2 "One of the first apps in the windows phone marketplace to successfully integrate the OpenCV library into the windows phone runtime. Used computer vision and a custom-trained neural net for reading sudoku puzzles from images. Ironically, hinting and solving a sudoku puzzle is far easier than locating and reading the puzzle from an image."
                :date "2014"
                :tech ["c++" "windows-phone" "neural-net" "opencv"]
                :project-url "#/oss"
                :picture "https://github.com/PeteW/TfsPivotViewer/raw/master/screenshot1.png"
                }
               {:title "TFS Pivot Viewer"
                :description "Navigate TFS work items using the powerful silverlight pivot viewer"
                :description2 "Silverlight, like windows phone, is a fascinating piece of outdated technological history. The power of XOML, the cross-platform runtime and the impressively polished development tools made it just plain fun to work with."
                :date "2014"
                :tech ["c#" "silverlight" "REST"]
                :project-url "https://github.com/PeteW/tfspivotviewer"
                :picture "https://github.com/PeteW/TfsPivotViewer/raw/master/screenshot1.png"
                }
               {:title "Fluent-Ibatis"
                :description "A port of the java Ibatis ORM to C# with additional fluent programmatic interface"
                :date "2013"
                :tech ["c#" "ORM"]
                :project-url "https://github.com/PeteW/fluentibatis"
                }
               {:title "reallifedata.com"
                :description "This site is an open-source project running on heroku"
                :description2 "Implementation in Reagent, a lisp-style language for React. Uses aws-browser-sdk automation for loading content."
                :date "2019"
                :tech ["clojurescript" "reagent" "aws" "reactjs" "heroku"]
                :project-url "https://github.com/PeteW/reallifedatadotcom"
                }
               {:title "ELMAH Extensions"
                :description "Built on top of ELMAH .NET (Error Logging Modules and Handlers) library. This extension adds support for rules-based configuration of how errors are handled such that different classes of exceptions can generate different logging actions and alert different recipients."
                :date "2013"
                :description2 "Flexible error handling and routing rules are ideal for enterprise and/or distributed software teams where different resources are tasked with handling different types of issues within a common web application."
                :tech ["c#" "ops"]
                :project-url "https://github.com/PeteW/ElmahExtensions"
                }
               {:title "git-tfs"
                :description "A utility for converting TFS source control to git"
                :date "2014"
                :description2 "git-tfs works by replaying the commit history of TFS into a git repository. At the time, git-tfs was unable to find TFS commits for deleted branches, this contribution was to alleviate the issue."
                :tech ["c#" "tfs" "git"]
                :project-url "https://github.com/PeteW/git-tfs/commit/78456ba20a1e667e25c3aa91acfb39b3ac6803ca"
                }
               ])


(defn view []
  [:div 
    [:p 
      [:strong "Disclosure: "]
      "I'm not a full-time OSS developer. I am an OSS enthisiast. The project list below omits work directly associated or exclusive property of clients and employers or older than 10 years."]
    [:div.row
     [:div.col-md-2
      [:div.panel.panel-default
       [:div.panel-heading "Date"]
       [:div.panel-body
         (for [t (cons "All" (sort (distinct (map :date projects))))]
           [:a {:href "#/oss"} [:div>span.badge {:on-click #(reset! projectfilter t)}(str t)]])
        ]
       ]
      [:div.panel.panel-default
       [:div.panel-heading "Tech"]
       [:div.panel-body
        (for [t (cons "All" (sort (distinct (flatten (map :tech projects)))))]
           [:a {:href "#/oss"} [:div>span.badge {:on-click #(reset! projectfilter t)}(str t)]])
        ]]
      ]
    [:div.col-md-10
      [XMasonry {:id "grid"
                 :center true
                 :responsive true
                 }
            (for [project (shuffle(filter #(or
                                             (= @projectfilter "All")
                                             (= @projectfilter (:date %))
                                             (contains? (set (:tech %)) @projectfilter)
                                             ) projects))]
              [XBlock {:key (:title project)}
                [:div.tile.panel.panel-default 
                   [:a {:href (:project-url project)
                        :target "_blank"}
                     (if (contains? project :picture)
                [:div.tileheader.panel-heading
                         [:img {:src (:picture project)
                                :style {:max-width "100%"
                                        :width "auto"
                                        :height "auto"
                                        }}]]
                         nil
                         )
                    [:div {:style {:background-color "#0c0"
                                           :color "#222"
                                           :padding "0px 5px"
                                           }}
                             [:h4 (:title project)]]
                   ]
                [:div.tilebody.panel-body (:description project)]
                (if (contains? project :description2)
                  [:div.tilebody2.panel-body (:description2 project)])
                (if (contains? project :tech)
                  [:div.tech (for [t (:tech project)]
                    [:a {:href "#/oss"} [:span.badge {:on-click #(reset! projectfilter t)}(str t)]]
                                             )])
                (if (contains? project :date)
                  [:div.tiledate "Date: "
                    [:a {:href "#/oss"} [:span.badge {:on-click #(reset! projectfilter (:date project))}(:date project)]]
                   ])
                 ]])]]]])
