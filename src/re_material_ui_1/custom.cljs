(ns re-material-ui-1.custom
  (:require [reagent.core :as r]
            [re-material-ui-1.core :as ui]))

(defn datatable [column-data data &{:keys [no-paging? rows-per-page identifier
                                           head-style cell-style title]
                                    :or {rows-per-page 5
                                         no-paging? false
                                         identifier :key}}]
  (let [state (r/atom {:order "asc" :page 0 :rows-per-page rows-per-page
                       :selected []})
        data-atom (atom data)]
    (fn []
      [ui/paper {:style {:width "100%"
                         :overflow-x "auto"}}
       [:div {:style {:flex "0 0 auto"}}
        (when title
          [ui/typography {:variant "title"
                          :style (merge {:padding-top "12px"
                                         :padding-left "22px"}
                                                  (dissoc head-style :font-size))}
           title])
        [:div {:style {:flex "1 1 100%"}}]]
       [:div {:style {:overflow-x "auto"}}
        [ui/table {:style {:min-width "700px"}}
         [ui/table-head
          [ui/table-row
           (doall
            (for [head column-data]
              (do
                (when (:eq head)
                  (reset! data-atom (doall
                                     (for [item @data-atom]
                                       (assoc item (:key head) ((:eq head) item))))))
                [ui/table-cell {:key (:key head)
                                :numeric (:numeric head)
                                :style (merge (if (:disable-padding head)
                                                {:padding "0"}
                                                {})
                                              head-style)}
                 (when (not (:numeric head))
                   (:label head))
                 (when (:sortable? head)
                   [ui/tooltip {:title "sort"
                                :enter-delay 300}
                    [ui/table-sort-label {:active (= (:order-by @state) (:key head))
                                          :direction (:order @state)
                                          :on-click #(swap! state assoc
                                                            :order-by (:key head)
                                                            :order (if (= (:order @state) "asc")
                                                                                    "desc"
                                                                                    "asc"))}]])
                 (when (:numeric head)
                   (:label head))])))]]
         [ui/table-body
          (doall
           (for [item (if no-paging?
                        (if (:order-by @state)
                          (sort-by (:order-by @state)
                                   (if (= (:order @state) "asc")
                                     < >) @data-atom)
                          @data-atom)
                        (if (:order-by @state)
                          (take (:rows-per-page @state) (drop (* (:page @state) (:rows-per-page @state))
                                                              (sort-by (:order-by @state)
                                                                       (if (= (:order @state) "asc")
                                                                         < >) @data-atom)))
                          (take (:rows-per-page @state) (drop (* (:page @state)  (:rows-per-page @state))
                                                              @data-atom))))]
             [ui/table-row {:key (identifier item)}
              (doall
               (for [[k v] (seq (select-keys item (map #(:key %) column-data)))
                     :let [head (first (filter #(= (:key %) k) column-data))]]       
                 [ui/table-cell {:key k
                                 :style (merge (if (:disable-padding head)
                                                 {:padding "0"}
                                                 {})
                                               cell-style)
                                 :numeric (:numeric head)
                                 :component (:component head)}
                  v]))]))]]]
       (when (not no-paging?)
         [ui/table-pagination {:component "div"
                               :count (count data)
                               :rows-per-page (:rows-per-page @state)
                               :page (:page @state)
                               :back-icon-button-props {:aria-label "Pervious page"}
                               :next-icon-button-props {:aria-label "Next page"}
                               :on-change-page #(swap! state assoc :page %2)
                               :on-change-rows-per-page #(swap! state assoc 
                                                                :rows-per-page (.. % -target -value))}])])))

(defn input-field [key input-atom & {:keys [class-name style on-change on-blur label
                                            required error-text  type placeholder
                                            helper-text rows multiline margin disabled
                                            start-adornment end-adornment shrink
                                            time-step]
                                     :or {rows "3"
                                          margin "normal"
                                          time-step "300"}}]
  
  [ui/form-control {:class-name class-name :style style :error (if error-text true false)
                    :margin margin :disabled disabled :required required}
   [ui/input-label (if shrink
                     {:html-for (name key) :shrink shrink}
                     {:html-for (name key)}) label]
   [ui/input {:value (key @input-atom)
              :on-change #(do
                            (swap! input-atom assoc key (.. % -target -value))
                            on-change)
              :type (if multiline "text" type)
              :on-blur on-blur
              :multiline multiline
              :placeholder placeholder
              :start-adornment (when start-adornment
                                 (r/create-element
                                  (aget js/MaterialUI (name :InputAdornment))
                                  #js {:position "start"} (if (string? start-adornment)
                                                            start-adornment
                                                            (r/as-element start-adornment))))
              :end-adornment (when end-adornment
                               (r/create-element
                                (aget js/MaterialUI (name :InputAdornment))
                                #js {:position "end"} (if (string? end-adornment)
                                                        end-adornment
                                                        (r/as-element end-adornment))))
              :rows rows :step (when (or (= type "time")
                                         (= type "datetime")) time-step)}]
   (when (or helper-text error-text)
     [ui/form-helper-text (if error-text error-text helper-text)])])

(defn select [key input-atom options & {:keys [class-name style on-change on-blur label
                                               required error-text margin disabled
                                               helper-text native-select? keys]
                                        :or {margin "normal"
                                             keys {:value :value :label :label}}}]
  (if native-select?
    [ui/form-control {:class-name class-name :style style :error (if error-text true false)
                      :margin margin :disabled disabled}
     [ui/input-label {:html-for (name key)} label]
     [ui/select {:native true
                 :value (key @input-atom)
                 :on-change #(do
                               (swap! input-atom assoc key (.. % -target -value))
                               on-change)
                 :on-blur on-blur
                 :input (r/create-element (aget js/MaterialUI (name :Input))
                                          #js{:id (name key)})}
      (doall
       (for [option options]
         [:option {:key ((:value keys) option) :value ((:value keys) option)}
          ((:label keys) option)]))]
     (when (or helper-text error-text)
       [ui/form-helper-text (if error-text error-text helper-text)])]
    [ui/text-field {:select true
                    :id key :label label :style style
                    :class-name class-name :disabled disabled
                    :required required :error (if error-text true false)
                    :value (if (not (key @input-atom))
                             ((:value keys) (first options))
                             (key @input-atom))
                    :on-change #(do
                                  (swap! input-atom assoc key (.. % -target -value))
                                  on-change)
                    :on-blur on-blur
                    :helperText (if error-text error-text helper-text)
                    :margin margin}
     (doall
      (for [option options]
        [ui/menu-item {:key ((:value keys) option) :value ((:value keys) option)}
         ((:label keys) option)]))]))
