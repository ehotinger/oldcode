#
# A simple Makefile to build 'esh'
#
LDFLAGS=
LDLIBS=-ll -ldl -lreadline -lcurses
# The use of -Wall, -Werror, and -Wmissing-prototypes is mandatory 
# for this assignment
CFLAGS=-Wall -Werror -Wmissing-prototypes -g -fPIC
#YFLAGS=-v

LIB_OBJECTS=list.o esh-utils.o esh-sys-utils.o
OBJECTS=esh.o
HEADERS=list.h esh.h esh-sys-utils.h
PLUGINDIR=plugins
PLUGIN_C=$(wildcard $(PLUGINDIR)/*.c)
PLUGIN_SO=$(patsubst %.c,%.so,$(PLUGIN_C))

default: esh $(PLUGIN_SO)

# rules to build plugins 
plugins/deadline.so: plugins/deadline.c
	gcc -g -Wall -shared -fPIC -o $@ -IcJSONFiles \
		$< libesh.a $(shell curl-config --libs) cJSONFiles/cJSON.c -lm

# The rules assume each plugin is in its own file
$(PLUGIN_SO): %.so : %.c
	gcc -Wall -shared -fPIC -o $@ $< libesh.a

$(LIB_OBJECTS) : $(HEADERS)

# build scanner and parser
esh-grammar.o: esh-grammar.y esh-grammar.l
	$(LEX) $(LFLAGS) $*.l
	$(YACC) $(YFLAGS) $<
	$(CC) -Dlint -c -o $@ $(CFLAGS) y.tab.c
	rm -f y.tab.c lex.yy.c

# build the shell
esh: libesh.a $(OBJECTS) $(HEADERS) esh-grammar.o
	$(CC) $(CFLAGS) -o $@ $(LDFLAGS) esh-grammar.o $(OBJECTS) libesh.a $(LDLIBS)

# build the supporting library
libesh.a: $(LIB_OBJECTS)
	ar cr $@ $(LIB_OBJECTS)
	ranlib $@

clean:
	rm -f $(OBJECTS) $(LIB_OBJECTS) esh esh-grammar.o \
		$(PLUGIN_SO) core.* libesh.a tests/*.pyc
