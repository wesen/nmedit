#!/usr/bin/perl

use strict;
use Carp;
use Data::Dumper;
$Data::Dumper::Quotekeys = 1;
$Data::Dumper::Varname = '';

my $filename = $ARGV[0];
my $subfile = $ARGV[1];
my $outfile = $ARGV[2];

die "Wrong usage: xmlize_module_table.pl filename substitution_file outfile\n" if !$filename||!$subfile ||!$outfile;

open(FILE, "$filename") || die "Cannot open $filename: $!\n";


my $module_table = {};
my $ctype;
my $paramode = 0;
my $paramtag;

while(<FILE>)
{
	my $line = $_;
	chomp $line;
	$line =~ s/\r//g;

	next if($line =~ /^;/);

	if($line =~ /------/)
	{
		$ctype = "";
		$paramode = 0;
		$paramtag = "";
	}

	my @splitted_line = split(/ /, $line);

	if($splitted_line[0] =~/\d+/ && !$paramode)
	{
		$ctype = $splitted_line[0];	
		$module_table->{$ctype}->{Name} = $splitted_line[1];
	}
	elsif(($splitted_line[0] eq 'remarks'
	|| $splitted_line[0] eq 'cycles'
	|| $splitted_line[0] eq 'progMem'
	|| $splitted_line[0] eq 'xmem'
	|| $splitted_line[0] eq 'ymem'
	|| $splitted_line[0] eq 'zeropage'
	|| $splitted_line[0] eq 'dynmem'
	|| $splitted_line[0] eq 'height') && !$paramode)
	{
		my $tag = shift @splitted_line;
		$module_table->{$ctype}->{$tag} = join ' ', @splitted_line;
		$module_table->{$ctype}->{$tag} =~ s/"//g;
	}	
	elsif($splitted_line[0] eq 'parameters')
	{
		$paramode = 1;
		$paramtag = 'standard';
	}
	elsif($splitted_line[0] eq 'inputs'
	|| $splitted_line[0] eq 'outputs'
	&& $paramode)
	{
		$paramtag = $splitted_line[0];
		$paramtag =~ s/s$//g;
		$paramtag = lc $paramtag;
		$module_table->{$ctype}->{Connectors} ||= {};
		$module_table->{$ctype}->{Connectors}->{$paramtag} ||= {};
	}
	elsif($splitted_line[0] eq 'custom')
	{
		$paramtag = $splitted_line[0];
		$paramtag = lc $paramtag;
		$module_table->{$ctype}->{Parameters}->{$paramtag} ||= {};
	}
	elsif($splitted_line[0] =~/\d+/ && $paramode)
	{
		my $param_no = shift @splitted_line;		
		
		if($paramtag eq 'standard' || $paramtag eq 'custom')
		{

			my ($param_name, $param_range, $bit_count, $param_short);

			if($line =~ /^(\d+) "([^"]*)" (\d+\.\.\d+) (\d+) "([^"]*)"$/)
			{
				$param_name = lc $2;
				$param_range = lc $3;
				$bit_count = lc $4;
				$param_short = lc $5;
			}	
			else
			{
				$line =~ /^(\d+) "([^"]*)" (\d+\.\.\d+) "([^"]*)"$/;
				$param_name = $2;
				$param_range = $3;
				$param_short = $4;
			}
				

			$module_table->{$ctype}->{Parameters}->{$paramtag}->{$param_no} =
			{
				'Name' => $param_name,
				'Range' => $param_range,
				'Remarks' => $param_short,
				'BitCount' => $bit_count,
			};	
		}
		else
		{
			my $param_name = shift @splitted_line;
			my $param_type =  shift @splitted_line;

			if($line =~ /^(\d+) "([^"]*)" (.*)$/)
			{
				$param_name = $2;
				$param_type = $3;
			}
			$param_name =~ s/"//g;
			$param_type =~ s/"//g;

			$module_table->{$ctype}->{Connectors}->{$paramtag}->{$param_no} =
			{
				'Name' => $param_name,
				'ConnectorType' => $param_type,
			};
		}
	}
}
close(FILE);

#print Dumper($module_table->{107});
#print Dumper($module_table);

my $xml = <<END;
<?xml version="1.0"?>
<!DOCTYPE ModuleDefinitions SYSTEM "module_definitions.dtd">
<ModuleDefinitions>
   <header>
      <version>14</version>
		<description>The NMEdit Modules definitions file</description>
   </header>
		
END

foreach my $module_id (sort { $a <=> $b } keys %$module_table)
{
	my $module = $module_table->{$module_id};


	$xml .= "	<module>\n";
	$xml .= <<END;
		<module_type_id>$module_id</module_type_id>
		<module_name><![CDATA[$module->{Name}]]></module_name>
		<remarks><![CDATA[$module->{remarks}]]></remarks>
		<cycles>$module->{cycles}</cycles>
		<progmem>$module->{progmem}</progmem>
		<xmem>$module->{xmem}</xmem>
		<ymem>$module->{ymem}</ymem>
		<zeropage>$module->{zeropage}</zeropage>
		<dynmem>$module->{dynmem}</dynmem>
		<height>$module->{height}</height>
END

	my $parameters = $module->{Parameters}->{standard};
	my $inputs = $module->{Connectors}->{input};
	my $outputs = $module->{Connectors}->{output};
	my $custom = $module->{Parameters}->{custom};

	$xml .= "		<parameters>\n";
	foreach my $param_id (sort keys %$parameters)
	{
		my $param = $parameters->{$param_id};
	
		my $identifier = "parameter-$module_id-standard-$param_id";

		$xml .= <<END;
			<parameter>
				<parameter_id>$param_id</parameter_id>
				<identifier>$identifier</identifier>
				<label><![CDATA[$param->{Name}]]></label>
				<value_range>$param->{Range}</value_range>
				<bit_count>$param->{BitCount}</bit_count>
				<remarks><![CDATA[$param->{Remarks}]]></remarks>
			</parameter>
END
	}
	$xml .= "		</parameters>\n";


	$xml .= "		<inputs>\n";
	foreach my $connector_id (sort keys %$inputs)
	{
		my $connector = $inputs->{$connector_id};
		my $connector_type = $connector->{ConnectorType};
		my $name = $connector->{Name};

		my $identifier = "connector-$module_id-input-$connector_id";

		$xml .= <<END;
			<connector>
				<connector_id>$connector_id</connector_id>
				<identifier>$identifier</identifier>
				<label><![CDATA[$name]]></label>
				<connector_type>$connector_type</connector_type>
			</connector>
END
	}
	$xml .= "		</inputs>\n";

	$xml .= "		<outputs>\n";
	foreach my $connector_id (sort keys %$outputs)
	{
		my $connector = $inputs->{$connector_id};
		my $connector_type = $connector->{ConnectorType};
		my $name = $connector->{Name};

		my $identifier = "connector-$module_id-output-$connector_id";

		$xml .= <<END;
			<connector>
				<connector_id>$connector_id</connector_id>
				<identifier>$identifier</identifier>
				<label><![CDATA[$name]]></label>
				<connector_type>$connector_type</connector_type>
			</connector>
END
	}
	$xml .= "		</outputs>\n";

	$xml .= "		<custom>\n";
	foreach my $param_id (sort keys %$custom)
	{
		my $param = $parameters->{$param_id};
	
		my $identifier = "parameter-$module_id-custom-$param_id";
		$xml .= <<END;
			<parameter>
				<parameter_id>$param_id</parameter_id>
				<identifier>$identifier</identifier>
				<label><![CDATA[$param->{Name}]]></label>
				<value_range>$param->{Range}</value_range>
				<remarks><![CDATA[$param->{Remarks}]]></remarks>
			</parameter>
END
	}
	$xml .= "		</custom>\n";

	$xml .= "	</module>\n";
}
$xml .= "</ModuleDefinitions>\n";


#Substitute
open(SUBFILE, "$subfile") || die "Can't open $subfile: $!\n";
while(<SUBFILE>)
{
	my $line = $_;
	my ($substr, $val) = split(/=/, $line);
	$xml =~ s/$substr/$val/g;
}
close SUBFILE;

open(OUTFILE, ">$outfile") || die "Can't open $outfile for writing: $!\n";
print OUTFILE $xml;
close OUTFILE;
