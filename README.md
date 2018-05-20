# reagent material ui 1
[![Clojars Project](https://img.shields.io/clojars/v/re-material-ui-1.svg)](https://clojars.org/re-material-ui-1)

Usage of [material-ui-1](https://material-ui.com/) components with reagent.

# Usage
 Add `[re-material-ui-1 "0.1.0-SNAPSHOT"]`to project.clj.

Then include [re-material-ui-1.core :as ui] like so:
```clojure
(ns reagent-material-ui-example.core
    (:require [re-material-ui-1.core :as ui]))
````
All components names uses kebab-case

#Example 

```clojure
(defn app-bar-example []
  [:div {:style {:flex-grow "1"}}
   [ui/app-bar {:position "static" :color "default"}
    [ui/toolbar
     [ui/typography {:variant "title" :color "inherit"}
      "Title"]]]]
      
(defn home-page []
  [ui/mui-theme-provider {:theme (ui/create-mui-theme-fn (clj->js {:palette {:type "light"}}))}
   [:div
    [app-bar-example]]])
 ```
Also the project holds custom components:

#Example with custom component
```clojure
(ns reagent-material-ui-1.examples.text-field
  (:require [reagent.core :as r]
            [re-material-ui-1.core :as ui]
            [re-material-ui-1.custom :as custom-ui]))
            
 (defn text-field-example1 []
  (let [input (r/atom {:error "wrong input"})
        style {:margin-left "12px"
               :margin-right "12px"
               :width "200px"}
        options [{:id "dollars" :shortcut "$"}
                 {:id "euros" :shortcut "€"}
                 {:id "BTC" :shortcut "฿"}
                 {:id "JPY" :shortcut "¥"}]]
    (fn []
      [:div {:style {:display "flex" :flex-wrap "wrap"}}
       [custom-ui/input-field :age input :label "Age" :style style :type "number" :end-adornment "years"]
       [custom-ui/select :select-native input options :style style :label "Native Select"
        :keys {:value :id :label :shortcut} :native-select? true]])))
```
 
 For more examples check the [reagent-material-ui-1 examples project](https://github.com/m-arch/reagent-material-ui-1).
 To see some running examples in action check [this](http://109.74.201.239:3000/) 
