#!/usr/bin/perl


use strict;
use Carp;
use Data::Dumper;

#use XML::XPath;
#use XML::XPath::XMLParser;
#
#
if(!$ARGV[0] || !$ARGV[1])
{
	die "Usage: query_module_definitions.pl filename XPATH_QUERY\n";
}
#
#my $xp = XML::XPath->new(filename => $ARGV[0]);
#
#my $nodeset = $xp->find($ARGV[1]);
#
#foreach my $node ( $nodeset->get_nodelist() ) 
#{
#	print XML::XPath::XMLParser::as_string( $node ) . "\n";
#}  


use XML::Twig;


my $buf;

my $twig = XML::Twig->new( TwigHandlers => {
			'zeropage[string() = "2.00000"]' => \&zeropage,
			#"module_type_id[text() = '127']" => \&idmatch,
			},
			pretty_print => 'indented',);

$twig->parsefile($ARGV[0]);

sub zeropage
{
	my ($tree, $element) = @_;
	$element->parent()->print();
	
}

sub idmatch
{
	my ($tree, $element) = @_;
}

