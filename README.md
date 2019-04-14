
### Local development
```
lein figwheel
```

### REPL

The project is setup to start nREPL on port `7002` once Figwheel starts.
Once you connect to the nREPL, run `(cljs)` to switch to the ClojureScript REPL.

### Building for SPA/html/js
```
lein clean
lein package
```
