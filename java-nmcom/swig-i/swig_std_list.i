// Java implementation

%{
#include <list>
#include <algorithm>
#include <stdexcept>
%}

// methods which can raise are caused to throw an IndexError
%exception std::list::get {
    try {
        $action
    } catch (std::out_of_range& e) {
        SWIG_exception(SWIG_IndexError,const_cast<char*>(e.what()));
    }
}
%exception std::list::removeItem {
    try {
        $action
    } catch (std::out_of_range& e) {
        SWIG_exception(SWIG_IndexError,const_cast<char*>(e.what()));
    }
}
// exported class

namespace std {

    template<class T> class list {
        // add generic typemaps here
      public:
        list();
        unsigned int size() const;
        %rename(isEmpty) empty;
        bool empty() const;
        void clear();
        %rename(add) push_back;
        void push_back(T& x);

        %extend {
          T& get(int i) const
          {
            if (i<0 || i>=self->size())
              throw new std::out_of_range("list index out of range");

            std::list<T>::iterator theIterator = self->begin();
            std::advance(theIterator,i);
            return *theIterator;
          }
          void removeItem(int i)
          {
            if (i<0 || i>=self->size())
              throw new std::out_of_range("list index out of range");

            std::list<T>::iterator theIterator = self->begin();
            std::advance(theIterator,i);
            self->erase(theIterator);
          }
        }
    };

    // specializations for built-ins

    %define specialize_std_list(T)
        template<> class list<T> {
        // add specialized typemaps here
      public:
        list();
        unsigned int size() const;
        %rename(isEmpty) empty;
        bool empty() const;
        void clear();
        %rename(add) push_back;
        void push_back(T x);
        %extend {
          T get(int i)
          {
            if (i<0 || i>=self->size())
              throw new std::out_of_range("list index out of range");

            std::list<T>::iterator theIterator = self->begin();
            std::advance(theIterator,i);
            return *theIterator;
          }
          void removeItem(int i)
          {
            if (i<0 || i>=self->size())
              throw new std::out_of_range("list index out of range");

            std::list<T>::iterator theIterator = self->begin();
            std::advance(theIterator,i);
            self->erase(theIterator);
          }
        }
    };
    %enddef

    specialize_std_list(bool);
    specialize_std_list(char);
    specialize_std_list(int);
    specialize_std_list(short);
    specialize_std_list(long);
    specialize_std_list(unsigned char);
    specialize_std_list(unsigned int);
    specialize_std_list(unsigned short);
    specialize_std_list(unsigned long);
    specialize_std_list(float);
    specialize_std_list(double);
    specialize_std_list(std::string);

}

