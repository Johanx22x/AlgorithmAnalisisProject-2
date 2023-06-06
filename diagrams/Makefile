SOURCE = $(wildcard *.puml)
OUT = $(patsubst %.puml,%.svg,$(SOURCE))

all: $(OUT)

%.svg: %.puml
	plantuml -tsvg $<
