;;
;;     MM""""""""`M
;;     MM  mmmmmmmM
;;     M`      MMMM 88d8b.d8b. .d8888b. .d8888b. .d8888b.
;;     MM  MMMMMMMM 88''88'`88 88'  `88 88'  `"" Y8ooooo.
;;     MM  MMMMMMMM 88  88  88 88.  .88 88.  ...       88
;;     MM        .M dP  dP  dP `88888P8 '88888P' '88888P'
;;     MMMMMMMMMMMM
;;
;;         M""MMMMMMMM M""M M""MMMMM""M MM""""""""`M
;;         M  MMMMMMMM M  M M  MMMMM  M MM  mmmmmmmM
;;         M  MMMMMMMM M  M M  MMMMP  M M`      MMMM
;;         M  MMMMMMMM M  M M  MMMM' .M MM  MMMMMMMM
;;         M  MMMMMMMM M  M M  MMP' .MM MM  MMMMMMMM
;;         M         M M  M M     .dMMM MM        .M
;;         MMMMMMMMMMM MMMM MMMMMMMMMMM MMMMMMMMMMMM  Version 1.0beta27
;;
;;           http://github.com/overtone/emacs-live
;;
;; Hello Asep, it's lovely to see you again. I do hope that you're well.
(ns geekcamp.core
  (:use [overtone.live]
        [overtone.inst.piano]
        [overtone.inst.drum]))

;; LET'S MAKE SOME NOISES
(definst sin-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (sin-osc freq)
     vol))

(sin-wave 440 4 1 1 1)

(definst square-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-pulse:ar freq)
     vol))

(square-wave)
(definst saw-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (saw freq)
     vol))

(saw-wave)

(definst triangle-wave [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (lf-tri freq)
     vol))

(triangle-wave)

(definst noisey [freq 440 attack 0.01 sustain 0.4 release 0.1 vol 0.4]
  (* (env-gen (lin attack sustain release) 1 1 0 1 FREE)
     (pink-noise)
     vol))

(noisey)

;; SAMPLE 1: Frequency Modulation Synthesis
(defsynth fm-synth [carrier 440 divisor 2.0 depth 1.0 out-bus 0]
  (let [modulator (/ carrier divisor)
        mod-env (env-gen (lin 2 0 6))
        amp-env (env-gen (lin 2 1 5) :action FREE)]
    (out out-bus (pan2 (* 0.5 amp-env
                          (sin-osc (+ carrier
                                      (* mod-env (* carrier depth) (sin-osc modulator)))))))))


(fm-synth)
;; SAMPLE 2: Play The Sampler
(kick)

(snare)

(piano)

(def guitar-lick (sample "lick.wav"))

(guitar-lick)

(defn play-chord [a-chord]
  (doseq [note a-chord] (piano note)))

(play-chord (chord :E4 :minor))
(play-chord (chord :C4 :major))
(play-chord (chord :G3 :major))
(play-chord (chord :D4 :major))

;; SAMPLE 3: Making A Sequencer
(def metro (metronome 128))

(defn left-hand [m beat-num]
  (at (m (+ 0 beat-num)) (play-chord (chord :E4 :minor)))
  (at (m (+ 4 beat-num)) (play-chord (chord :C4 :major)))
  (at (m (+ 8 beat-num)) (play-chord (chord :G3 :major)))
  (at (m (+ 12 beat-num)) (play-chord (chord :D4 :major)))
  (apply-at (m (+ 16 beat-num)) left-hand m (+ 16 beat-num) []))

(defn right-hand [m beat-num]
  (at (m (+ 0 beat-num)) (piano (note :G6)))
  (at (m (+ 2 beat-num)) (piano (note :B6)))
  (at (m (+ 4 beat-num)) (piano (note :E6)))
  (at (m (+ 8 beat-num)) (piano (note :F#6)))
  (at (m (+ 12 beat-num)) (piano (note :D6)))
  (apply-at (m (+ 16 beat-num)) right-hand m (+ 16 beat-num) []))

(defn drum-loop [m beat-num]
  (at (m (+ 0 beat-num)) (kick))
  (at (m (+ 3 beat-num)) (kick))
  (at (m (+ 4 beat-num)) (snare))
  (apply-at (m (+ 8 beat-num)) drum-loop m (+ 8 beat-num) []))

(stop)
(left-hand metro (metro))

(right-hand metro (metro))

(drum-loop metro (metro))

(fm-synth 329.6)

(fm-synth 392)
